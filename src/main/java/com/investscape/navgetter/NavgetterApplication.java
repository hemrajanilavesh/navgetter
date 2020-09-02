package com.investscape.navgetter;

import com.investscape.navgetter.service.NavService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.IOException;

@Slf4j
@SpringBootApplication
public class NavgetterApplication {

	@Autowired
	private NavService navService;

	public static void main(String[] args) {
		SpringApplication.run(NavgetterApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void loadAllFunds() throws IOException {
		navService.loadNavForAllFunds();
	}

}
