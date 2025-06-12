package me.jeremymegyesi.CharonCore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"me.jeremymegyesi.CharonCore", "me.jeremymegyesi.CharonCommon"})
@EntityScan(basePackages = {"me.jeremymegyesi.CharonCore", "me.jeremymegyesi.CharonCommon"})
@EnableJpaRepositories(basePackages = {"me.jeremymegyesi.CharonCore", "me.jeremymegyesi.CharonCommon"})
public class CharonCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(CharonCoreApplication.class, args);
	}
}
