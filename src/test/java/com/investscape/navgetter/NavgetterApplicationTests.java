package com.investscape.navgetter;

import com.investscape.navgetter.service.NavService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class NavgetterApplicationTests {

	@Autowired
	CacheManager cacheManager;

	@Autowired
	NavService navService;

	@Test
	void contextLoads() {
		 assertThat(cacheManager).isNotNull();
		 assertThat(navService).isNotNull();
	}

}
