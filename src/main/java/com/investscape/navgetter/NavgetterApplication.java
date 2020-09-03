package com.investscape.navgetter;

import com.investscape.navgetter.service.NavService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class NavgetterApplication {

	@Autowired
	private NavService navService;

	public static void main(String[] args) {
		SpringApplication.run(NavgetterApplication.class, args);
	}

	@Scheduled(initialDelay = 10000, fixedDelayString = "${reload-interval.milliseconds:1800000}")
	public void regularlyLoadAllFunds() throws IOException {
		navService.loadNavForAllFunds();
	}

}
