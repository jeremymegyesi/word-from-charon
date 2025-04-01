package me.jeremymegyesi.CharonCore;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("/api")
public class CharonCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(CharonCoreApplication.class, args);
	}

	@GetMapping("/currentPosition")
	public String home() throws IOException {
		// FerrySchedule schedule = FerrySchedule.getInstance();
		// schedule.updateSchedule();
		// JSONObject obj = new JSONObject();
		// obj.put("latitude", schedule.latitude);
		// obj.put("longitude", schedule.longitude);
		// obj.put("progress", schedule.progress);
		return "abc";
	}
}
