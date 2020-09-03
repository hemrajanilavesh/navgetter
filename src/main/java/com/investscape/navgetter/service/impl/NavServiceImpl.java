package com.investscape.navgetter.service.impl;

import com.investscape.navgetter.model.Scheme;
import com.investscape.navgetter.service.NavService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class NavServiceImpl implements NavService {

    private static final String AMFI_WEBSITE_LINK = "https://www.amfiindia.com/spages/NAVAll.txt";
    private static final String SEPARATOR = ";";
    private static final String FUNDS_CACHE = "fundsCache";
    public static final String FUNDS_CACHE_KEY = "#schemeCode";

    // Return value from cache when forceReload = false
    // Execute the method and do not use cache when forceReload = true
    public static final String DO_NOT_USE_CACHE_ON_FORCE_RELOAD = "!#forceReload";
    public static final Scheme EMPTY_SCHEME = new Scheme(null, null, null, null);

    private Map<String, Scheme> fundsMap = new HashMap<>();

    @Autowired
    CacheManager cacheManager;

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
}
