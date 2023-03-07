package com.example.demo.employee;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

import static java.time.Month.JANUARY;

@Configuration
public class employeeConfig {

    @Bean
    CommandLineRunner commandLineRunner(
            employeeRepository repository) {
        return args -> {
            employee mariam = new employee(
                    "Mariam",
                    "mariam.jamal@gmail.com",
                    LocalDate.of(2000, JANUARY, 5)
            );

            employee alex = new employee(
                    "Alex",
                    "alex@gmail.com",
                    LocalDate.of(2004 , JANUARY, 5)
            );

            repository.saveAll(
                    List.of(mariam, alex)
            );
        };
    }
}
