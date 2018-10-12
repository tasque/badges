package org.badges;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class BadgesApplication {

    public static void main(String[] args) {
        SpringApplication.run(BadgesApplication.class, args);
    }
}
