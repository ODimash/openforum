package odimash.openforum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("odimash.openforum.domain.repository")
@EntityScan("odimash.openforum.domain.entity")

public class OpenForumApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenForumApplication.class, args);
	}

}
