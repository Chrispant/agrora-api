package gr.agrora_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication
public class AgroraApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgroraApiApplication.class, args);
	}


}

