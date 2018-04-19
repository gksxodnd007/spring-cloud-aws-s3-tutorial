package org.codingsquid.aws_s3_tutorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackageClasses = { AwsS3TutorialApplication.class, Jsr310JpaConverters.class })
@EnableJpaRepositories(basePackages = "org.codingsquid.aws_s3_tutorial")
public class AwsS3TutorialApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwsS3TutorialApplication.class, args);
	}
}
