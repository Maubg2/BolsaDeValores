package co.unbosque.bolsavalores.bolsadevalores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BolsadevaloresApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(BolsadevaloresApplication.class, args);
		
	}

}
