package com.fs121.monitor.web.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class AtmosphericElectricSchedule {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	// TODO
	@Scheduled(cron = "5 1 * * * ?") // 每1分钟执行一次
	public void scheduler() {
		logger.info(">>>>>>>>>>>>> scheduled ... ");
	}

}
