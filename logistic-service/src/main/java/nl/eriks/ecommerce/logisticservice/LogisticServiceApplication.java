package nl.eriks.ecommerce.logisticservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * A Rabbitmq application that listens to the order events
 */
@SpringBootApplication
public class LogisticServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogisticServiceApplication.class, args);
    }

}
