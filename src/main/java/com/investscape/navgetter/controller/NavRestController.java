package com.investscape.navgetter.controller;

import com.investscape.navgetter.model.Scheme;
import com.investscape.navgetter.service.NavService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

import java.io.IOException;

@RestController
public class NavRestController {

    @Autowired
    private NavService navService;

    @GetMapping(path = "/getNAV/{schemeCode}")
    @Operation(summary = "Fetch latest NAV. Sample schemeCode = 119551")
    public Scheme getScheme(@PathVariable String schemeCode,
                            @RequestParam(value = "forceUpdate", required = false, defaultValue = "false") boolean forceUpdate) throws IOException {

        return navService.getNav(forceUpdate, schemeCode);
    }

    @GetMapping(path = "/getNav/{schemeCode}/{date}")
    @Operation(summary = "Fetch NAV on date DD-MM-YYYY. Sample schemeCode = 119551, Sample date = 20-01-2020")
    public Scheme getSchemeNavOnDate(@PathVariable String schemeCode,
                                    @PathVariable String date) {
        return navService.getNavOnDate(schemeCode, date);
    }
}
