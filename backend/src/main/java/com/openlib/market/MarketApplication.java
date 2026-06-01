package com.openlib.market;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

@SpringBootApplication
@EnableScheduling
public class MarketApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarketApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MarketApplication.class, args);
    }

}
