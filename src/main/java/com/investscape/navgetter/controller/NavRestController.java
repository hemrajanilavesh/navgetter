package com.investscape.navgetter.controller;

import com.investscape.navgetter.model.Scheme;
import com.investscape.navgetter.service.NavService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class NavRestController {

    @Autowired
    private NavService navService;

    @GetMapping(path = "/getNAV/{schemeCode}")
    public Scheme getScheme(@ApiParam(value = "Schema code of a Mutual Fund", defaultValue = "119551") @PathVariable String schemeCode,
                            @ApiParam(value = "Parameter to force reload of all funds from source.") @RequestParam(value = "forceUpdate", required = false, defaultValue = "false") boolean forceUpdate) throws IOException {

        return navService.getNav(forceUpdate, schemeCode);
    }

    @GetMapping(path = "/getNav/{schemeCode}/{date}")
    public Scheme getSchemeNavOnDate(@ApiParam(value = "Schema code of a Mutual Fund", defaultValue = "119551") @PathVariable String schemeCode,
                                     @ApiParam(value = "Date to fetch NAV for, in format DD-MM-YYYY", defaultValue = "31-12-2019") @PathVariable String date) {
        return navService.getNavOnDate(schemeCode, date);
    }
}
