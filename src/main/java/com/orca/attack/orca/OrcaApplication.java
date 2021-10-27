package com.orca.attack.orca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.orca.attack.orca"})
public class OrcaApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrcaApplication.class, args);
    }

}
