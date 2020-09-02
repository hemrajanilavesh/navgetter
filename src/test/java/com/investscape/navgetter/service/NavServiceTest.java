package com.investscape.navgetter.service;

import com.investscape.navgetter.NavgetterApplication;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureRule;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NavgetterApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class NavServiceTest {

    @Autowired
    MockMvc mockMvc;

    @LocalServerPort
    private int port;

    @Rule
    public OutputCaptureRule outputCaptureRule = new OutputCaptureRule();

    private final UriTemplate GET_NAV_URI = new UriTemplate("http://localhost:{port}/getNAV/{schemeCode}?forceUpdate={forceUpdate}");

    @Test
    public void testGetNavWithReload() throws Exception {
        String testSchemeCode = "119551";
        mockMvc.perform(get(GET_NAV_URI.expand(port, testSchemeCode, true))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.schemeCode").exists())
                .andExpect(jsonPath("$.schemeCode").value(testSchemeCode))
                .andExpect(jsonPath("$.schemeName").exists())
                .andExpect(jsonPath("$.schemeName").value("Aditya Birla Sun Life Banking & PSU Debt Fund  - Direct Plan-Dividend"))
                .andExpect(jsonPath("$.nav").exists())
                .andExpect(jsonPath("$.date").exists());

        assertThat(outputCaptureRule.toString()).contains("Loading All Funds...");
        assertThat(outputCaptureRule.toString()).contains("All Funds loaded in ");
        assertThat(outputCaptureRule.toString()).contains("Looking for fund with Scheme Code: " + testSchemeCode);

    }
}