package com.openlib.market;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
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

    @Bean
    public ApplicationRunner datasourceTraceRunner(DataSource dataSource) {
        return args -> {
            try (Connection connection = dataSource.getConnection()) {
                DatabaseMetaData metadata = connection.getMetaData();
                String url = metadata.getURL();
                LOGGER.info("[PERSISTENCIA] Datasource activo. url={}, driver={}, user={}",
                        url,
                        metadata.getDriverName(),
                        metadata.getUserName());

                if (url != null && url.contains("jdbc:h2:mem:")) {
                    LOGGER.warn("[PERSISTENCIA] La base actual es H2 en memoria. Los datos se pierden al reiniciar el servidor.");
                }
            } catch (Exception e) {
                LOGGER.error("[PERSISTENCIA] No fue posible obtener metadata del datasource activo", e);
            }
        };
    }
}
