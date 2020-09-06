package com.investscape.navgetter.service.impl;

import com.investscape.navgetter.model.Scheme;
import com.investscape.navgetter.service.NavService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Slf4j
@Service
public class NavServiceImpl implements NavService {

    public static final String FUNDS_CACHE_KEY = "#schemeCode";
    // Return value from cache when forceReload = false
    // Execute the method and do not use cache when forceReload = true
    public static final String DO_NOT_USE_CACHE_ON_FORCE_RELOAD = "!#forceReload";
    public static final Scheme EMPTY_SCHEME = new Scheme(null, null, null, null);
    private static final String AMFI_WEBSITE_LINK = "https://www.amfiindia.com/spages/NAVAll.txt";
    private static final String MFAPI_WEBSITE_BASE_URL = "https://api.mfapi.in/mf/";
    private static final String DATE_PATTERN_DD_MM_YYYY = "dd-MM-yyyy";
    private static final String SEPARATOR = ";";
    private static final String FUNDS_CACHE = "fundsCache";
    @Autowired
    CacheManager cacheManager;
    private Map<String, Scheme> fundsMap = new HashMap<>();

    @Override
    public boolean loadNavForAllFunds() throws IOException {
        long start = System.currentTimeMillis();
        log.info("Loading All Funds...");
        URL url = new URL(AMFI_WEBSITE_LINK);
        List<String> lines = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }
        }
        lines.stream()
                .map(line -> line.split(SEPARATOR))
                .filter(elements -> elements.length >= 5)
                .forEach(elements -> {
                    fundsMap.put(elements[0], new Scheme(elements[0], elements[3], elements[4], elements[5]));
                });
        log.info("All Funds loaded in {} milliseconds.", (System.currentTimeMillis() - start));
        cacheManager.getCache(FUNDS_CACHE).clear();
        return true;
    }

    @Cacheable(value = FUNDS_CACHE, key = FUNDS_CACHE_KEY, condition = DO_NOT_USE_CACHE_ON_FORCE_RELOAD)
    @Override
    public Scheme getNav(boolean forceReload, String schemeCode) throws IOException {
        if (forceReload) {
            loadNavForAllFunds();
        }
        log.info("Looking for fund with Scheme Code: {}", schemeCode);
        Scheme scheme = fundsMap.getOrDefault(schemeCode, EMPTY_SCHEME);
        if (scheme.getSchemeCode() != null) {
            cacheManager.getCache(FUNDS_CACHE).put(schemeCode, scheme);
        }
        return scheme;
    }

    @Override
    public Scheme getNavOnDate(String schemeCode, String inputDate) {
        try {
            String date = getAdjustedDateForNAV(inputDate);
            String jsonResponse = getResponseFromURL(MFAPI_WEBSITE_BASE_URL + schemeCode);
            String navForDate = getNavFromJsonResponse(jsonResponse, date);
            String fundName = fundsMap.getOrDefault(schemeCode, EMPTY_SCHEME).getSchemeName();
            return new Scheme(schemeCode, fundName, navForDate, date);
        } catch (Exception e) {
            log.error("Exception while getting NAV from MFAPI: {}", e.getMessage());
            e.printStackTrace();
        }
        return EMPTY_SCHEME;
    }


    public String getAdjustedDateForNAV(String inputDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN_DD_MM_YYYY);
        LocalDate adjustedDate = LocalDate.parse(inputDate, formatter);
        if (adjustedDate.getDayOfWeek() == DayOfWeek.SATURDAY || adjustedDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            adjustedDate = adjustedDate.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
        }
        return adjustedDate.format(formatter);
    }


    public String getResponseFromURL(String URL) throws IOException {
        StringBuilder response = new StringBuilder();
        URL url = new URL(URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        if (conn.getResponseCode() != 200) {
            log.error("Request for URL: {} failed. Response Code: [{}]", URL, conn.getResponseCode());

        } else {
            log.info("Request for URL: [{}] successful.", url);
            Scanner sc = new Scanner(url.openStream());
            while (sc.hasNext()) {
                response.append(sc.nextLine());
            }
        }

        return response.toString();
    }


    public String getNavFromJsonResponse(String response, String inputDate) {
        JSONObject jsonObject = new JSONObject(response);
        JSONArray data = (JSONArray) jsonObject.get("data");
        for (int i = 0; i < data.length(); i++) {
            JSONObject apiResponseRow = data.getJSONObject(i);
            if (apiResponseRow.getString("date").equalsIgnoreCase(inputDate)) {
                return apiResponseRow.getString("nav");
            }
        }
        return "";
    }


}
