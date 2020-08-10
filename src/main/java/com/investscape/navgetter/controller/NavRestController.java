package com.investscape.navgetter.controller;

import com.investscape.navgetter.model.Scheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

@RestController
public class NavRestController {
    @Autowired
    private Environment env;

    private Long lastUpdated;

    private ArrayList<String> mfList;

    private static final String AMFI_WEBSITE_LINK = "https://www.amfiindia.com/spages/NAVAll.txt?t=11012017122537";

    @GetMapping(path = "/getNAV/{schemeCode}")
    public Scheme getScheme(@PathVariable String schemeCode) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        if (lastUpdated != null
                && System.currentTimeMillis() - lastUpdated < Long.parseLong(env.getProperty("forceUpdateTimeout"))) {
            lines = mfList;
        }
        else{
            URL url = new URL(AMFI_WEBSITE_LINK);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }
            mfList = lines;
            lastUpdated = System.currentTimeMillis();
            in.close();
        }

        for(String mf: lines) {
            if(mf.startsWith(schemeCode)){
                String[] elements = mf.split(";");
                return new Scheme(elements[0], elements[3], elements[4], elements[5]);
            }
        }
        return new Scheme(null,null,null,null);
    }
}
