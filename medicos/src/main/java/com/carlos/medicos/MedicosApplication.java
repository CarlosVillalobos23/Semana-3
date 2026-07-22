package com.carlos.medicos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages ={"com.carlos.medicos, com.carlos.commons"} )
public class MedicosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicosApplication.class, args);
	}

}
