package com.carlos.citas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages ={"com.carlos.citas ", "com.carlos.commons"} )

@EnableFeignClients(basePackages = "com.carlos.citas")
public class CitasApplication {

	public static void main(String[] args) {
		SpringApplication.run(CitasApplication.class, args);
	}

}
