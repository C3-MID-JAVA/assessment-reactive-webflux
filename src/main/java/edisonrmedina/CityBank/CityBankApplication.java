package edisonrmedina.CityBank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "edisonrmedina.CityBank")
public class CityBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(CityBankApplication.class, args);
	}

}
