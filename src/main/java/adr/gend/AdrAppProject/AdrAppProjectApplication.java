package adr.gend.AdrAppProject;

import jakarta.persistence.Entity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan("adr.gend.AdrAppProject.entity")
@SpringBootApplication
public class AdrAppProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdrAppProjectApplication.class, args);
	}


}
