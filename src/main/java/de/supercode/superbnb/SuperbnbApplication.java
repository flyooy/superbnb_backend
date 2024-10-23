package de.supercode.superbnb;

import de.supercode.superbnb.configuration.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class SuperbnbApplication {

	public static void main(String[] args) {
		SpringApplication.run(SuperbnbApplication.class, args);
	}

}
