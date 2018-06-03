package org.badges;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class DatasourceConfig {


    @Bean(/*initMethod = "start", */destroyMethod = "close")
    public PostgreSQLContainer postgresContainer() {
        PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer();
        postgreSQLContainer.start();
        return postgreSQLContainer;
    }

    @Bean
    public DataSource dataSource(PostgreSQLContainer postgresContainer) throws IOException {

        return DataSourceBuilder.create()
                .url(postgresContainer.getJdbcUrl())
                .password(postgresContainer.getPassword())
                .username(postgresContainer.getUsername())
                .build();
    }
}
