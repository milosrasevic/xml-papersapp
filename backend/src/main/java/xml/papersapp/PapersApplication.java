package xml.papersapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

import static xml.papersapp.util.DatabaseConnection.createDBConnection;

@SpringBootApplication
public class PapersApplication {

	public static void main(String[] args) {
		SpringApplication.run(PapersApplication.class, args);
	}

	@PostConstruct
	public static void createDB(){
		try {
			createDBConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
