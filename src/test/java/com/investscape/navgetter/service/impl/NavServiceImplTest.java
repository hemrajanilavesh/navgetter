package com.investscape.navgetter.service.impl;

import com.google.common.io.CharStreams;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

@SpringBootTest
public class NavServiceImplTest {
    NavServiceImpl navService = new NavServiceImpl();

    @Test
    public void testGetAdjustedDateForNAV() {
        Assertions.assertThat(navService.getAdjustedDateForNAV("29-08-2020")).isEqualTo("28-08-2020");
        Assertions.assertThat(navService.getAdjustedDateForNAV("28-08-2020")).isEqualTo("28-08-2020");
    }

    @Test
    public void testGetResponseFromUrl() throws IOException {
        String response = navService.getResponseFromURL("https://jsonplaceholder.typicode.com/todos/1");
        Assertions.assertThat(response).isNotEmpty();

        response = navService.getResponseFromURL("https://api.mfapi.in/mf/abcdef");
        Assertions.assertThat(response).isEmpty();
    }

    @Test
    public void testJsonParsing() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("mockApiResponse.json");
        String text = null;
        try (Reader reader = new InputStreamReader(inputStream)) {
            text = CharStreams.toString(reader);
        }

        Assertions.assertThat(navService.getNavFromJsonResponse(text, "01-01-2020")).isEqualTo("456");
        Assertions.assertThat(navService.getNavFromJsonResponse(text, "01-01-2019")).isEqualTo("");
    }

    @Test
    public void testNavOnDate() {
        Assertions.assertThat(navService.getNavOnDate("118989", "02-09-2020").getNav()).
                isEqualTo("58.43200");
        Assertions.assertThat(navService.getNavOnDate("-1", "20-11-1996").getNav())
                .isNull();

    }
}
