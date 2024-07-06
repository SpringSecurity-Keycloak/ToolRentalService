package io.rentalapp;

import io.rentalapp.model.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.rentalapp.configuration.LocalDateTimeConverter;
import io.rentalapp.configuration.LocalDateConverter;

import io.rentalapp.persist.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@ComponentScan(basePackages = { "io.rentalapp"})
public class Swagger2SpringBoot implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Swagger2SpringBoot.class);

    @Autowired
    ToolRepository repository;

    @Override
    public void run(String... arg0) throws Exception {
        if (arg0.length > 0 && arg0[0].equals("exitcode")) {
            throw new ExitException();
        }
    }

    public static void main(String[] args) throws Exception {
        new SpringApplication(Swagger2SpringBoot.class).run(args);
    }

    @Configuration
    static class CustomDateConfig extends WebMvcConfigurerAdapter {
        @Override
        public void addFormatters(FormatterRegistry registry) {
            registry.addConverter(new LocalDateConverter("yyyy-MM-dd"));
            registry.addConverter(new LocalDateTimeConverter("yyyy-MM-dd'T'HH:mm:ss.SSS"));
        }
    }

    class ExitException extends RuntimeException implements ExitCodeGenerator {
        private static final long serialVersionUID = 1L;

        @Override
        public int getExitCode() {
            return 10;
        }

    }

    @Bean
    public CommandLineRunner demo(ToolRepository repository) {
        return (args) -> {

            repository.save(new Tool("CHNS","Chainsaw","Stihl"));
            repository.save(new Tool("LADW","Ladder","Werner"));
            repository.save(new Tool("JAKD","Jackhammer","DeWalt"));
            repository.save(new Tool("JAKR","Jackhammer","Ridgid"));

            // fetch all customers
            log.info("Customers found with findAll():");
            log.info("-------------------------------");
            repository.findAll().forEach(tool -> {
                log.info(tool.toString());
            });
            log.info("");
        };
    }
}
