package eu_job_agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.jobagent", "eu_job_agent"})
@EnableJpaRepositories(basePackages = "com.jobagent")
@EntityScan(basePackages = "com.jobagent")
public class EuJobAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(EuJobAgentApplication.class, args);
    }
}