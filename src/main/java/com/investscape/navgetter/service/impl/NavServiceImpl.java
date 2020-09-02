package com.investscape.navgetter.service.impl;

import com.investscape.navgetter.model.Scheme;
import com.investscape.navgetter.service.NavService;
import lombok.extern.slf4j.Slf4j;
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

    private Map<String, Scheme> findMap = new HashMap<>();

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
                .map(line -> line.split(";"))
                .filter(elements -> elements.length >= 5)
                .forEach(elements -> {
                    findMap.put(elements[0], new Scheme(elements[0], elements[3], elements[4], elements[5]));
                });
        log.info("All Funds loaded in {} milliseconds.", (System.currentTimeMillis() - start));
        return true;
    }

    @Override
    public Scheme getNav(boolean forceReload, String schemeCode) throws IOException {
        if (forceReload) {
            loadNavForAllFunds();
        }
        return findMap.getOrDefault(schemeCode, new Scheme(schemeCode, "Not Found", "Not Found", "Not Found"));
    }
}
