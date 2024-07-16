package io.rentalapp;

import io.rentalapp.persist.ToolRentalPriceRepositorty;
import io.rentalapp.persist.ToolRepository;
import io.rentalapp.persist.entity.ToolEntity;
import io.rentalapp.persist.entity.ToolRentalPriceEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BootStrap {

    @Autowired
    ToolRepository repository;

    @Autowired
    ToolRentalPriceRepositorty toolRentalPriceRepositorty;

    private static final Logger log = LoggerFactory.getLogger(BootStrap.class);

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
            repository.save(new ToolEntity("CHNS","Chainsaw","Stihl"));
            repository.save(new ToolEntity("LADW","Ladder","Werner"));
            repository.save(new ToolEntity("JAKD","Jackhammer","DeWalt"));
            repository.save(new ToolEntity("JAKR","Jackhammer","Ridgid"));

            /*
             * Load Data for Tool Pricing
             *
             */
            toolRentalPriceRepositorty.save(new ToolRentalPriceEntity("Ladder"
                    , BigDecimal.valueOf(1.99)
                    ,true,
                    true,
                    false));
            toolRentalPriceRepositorty.save(new ToolRentalPriceEntity("Chainsaw"
                    , BigDecimal.valueOf(1.49)
                    ,true,
                    false,
                    true));
            toolRentalPriceRepositorty.save(new ToolRentalPriceEntity("Jackhammer"
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
