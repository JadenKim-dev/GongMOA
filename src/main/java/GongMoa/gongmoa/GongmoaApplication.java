package GongMoa.gongmoa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GongmoaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GongmoaApplication.class, args);
	}

}
