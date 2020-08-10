package com.investscape.navgetter;

import com.investscape.navgetter.model.Scheme;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@RestController
public class RequestController {

    @GetMapping(path = "/getNAV/{schemeCode}")
    public Scheme getScheme(@PathVariable String schemeCode) throws IOException {
        URL url = new URL("https://www.amfiindia.com/spages/NAVAll.txt?t=11012017122537");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

        String line;
        while ((line = in.readLine()) != null) {
            if(line.startsWith(schemeCode)){
                String[] elements = line.split(";");
                return new Scheme(elements[0], elements[3], elements[4], elements[5]);
            }
        }
        in.close();
        return new Scheme(null,null,null,null);
    }

}
