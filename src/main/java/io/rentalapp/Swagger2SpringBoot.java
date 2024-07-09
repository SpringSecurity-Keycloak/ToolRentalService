package io.rentalapp;

import io.rentalapp.configuration.LocalDateConverter;
import io.rentalapp.configuration.LocalDateTimeConverter;
import io.rentalapp.persist.ToolRentalPriceRepositorty;
import io.rentalapp.persist.ToolRepository;
import io.rentalapp.persist.model.ToolDTO;
import io.rentalapp.persist.model.ToolRentalPriceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.math.BigDecimal;

@SpringBootApplication
@ComponentScan(basePackages = { "io.rentalapp"})
public class Swagger2SpringBoot implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Swagger2SpringBoot.class);

    @Autowired
    ToolRepository repository;

    @Autowired
    ToolRentalPriceRepositorty toolRentalPriceRepositorty;

    /**
     *
     * @param arg0
     * @throws Exception
     */
    @Override
    public void run(String... arg0) throws Exception {
        if (arg0.length > 0 && arg0[0].equals("exitcode")) {
            throw new ExitException();
        }
    }

    /**
     *
     * @param args
     * @throws Exception
     */
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

    /**
     *
     */
    class ExitException extends RuntimeException implements ExitCodeGenerator {
        private static final long serialVersionUID = 1L;

        @Override
        public int getExitCode() {
            return 10;
        }

    }

    /**
     *
     * @param repository
     * @return
     */
    @Bean
    public CommandLineRunner demo(ToolRepository repository) {
        return (args) -> {

            /*
             * Load data for Tool
             */
            repository.save(new ToolDTO("CHNS","Chainsaw","Stihl"));
            repository.save(new ToolDTO("LADW","Ladder","Werner"));
            repository.save(new ToolDTO("JAKD","Jackhammer","DeWalt"));
            repository.save(new ToolDTO("JAKR","Jackhammer","Ridgid"));

            /*
             * Load Data for Tool Pricing
             *
             */
            toolRentalPriceRepositorty.save(new ToolRentalPriceDTO("Ladder"
                    , BigDecimal.valueOf(1.99)
                    ,true,
                    true,
                    false));
            toolRentalPriceRepositorty.save(new ToolRentalPriceDTO("Chainsaw"
                    , BigDecimal.valueOf(1.49)
                    ,true,
                    false,
                    true));
            toolRentalPriceRepositorty.save(new ToolRentalPriceDTO("Chainsaw"
                    , BigDecimal.valueOf(2.99)
                    ,true,
                    false,
                    false));


            // fetch all Tools available for rent
            log.info("Tools found with findAll():");
            log.info("-------------------------------");
            repository.findAll().forEach(tool -> {
                log.info(tool.toString());
            });
            // fetch pricing for each tool type
            toolRentalPriceRepositorty.findAll().forEach(toolType -> {
                log.info(toolType.toString());
            });
            log.info("");
        };
    }
}
