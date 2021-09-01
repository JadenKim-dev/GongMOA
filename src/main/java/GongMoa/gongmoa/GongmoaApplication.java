package GongMoa.gongmoa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.convert.Jsr310Converters;

@SpringBootApplication
public class GongmoaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GongmoaApplication.class, args);
	}

}
