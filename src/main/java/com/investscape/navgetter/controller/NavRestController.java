package com.investscape.navgetter.controller;

import com.investscape.navgetter.model.Scheme;
import com.investscape.navgetter.service.NavService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.io.IOException;

@OpenAPIDefinition(info = @Info(title = "NAVseeker", version = "1.0.0"))
@RestController
public class NavRestController {

    @Autowired
    private NavService navService;

    @GetMapping(path = "/getNAV/{schemeCode}")
    @Operation(summary = "Fetch the latest NAV from AMFI website.")
    public Scheme getScheme(
            @Parameter(description = "scheme Code for mutual fund", example = "120503") @PathVariable(value = "schemeCode") String schemeCode,
            @Parameter(description = "force update of NAV?", example = "false") @RequestParam(value = "forceUpdate", required = false, defaultValue = "false") boolean forceUpdate)
            throws IOException {

        return navService.getNav(forceUpdate, schemeCode);
    }

    @GetMapping(path = "/getNav/{schemeCode}/{date}")
    @Operation(summary = "Fetch NAV on date DD-MM-YYYY (or the last working day before DD-MM-YYYY).")
    public Scheme getSchemeNavOnDate(
            @Parameter(description = "scheme Code for mutual fund", example = "120503") @PathVariable String schemeCode,
            @Parameter(description = "date", example = "20-01-2020") @PathVariable String date) {
        return navService.getNavOnDate(schemeCode, date);
    }
}
