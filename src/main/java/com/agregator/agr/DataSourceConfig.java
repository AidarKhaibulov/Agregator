package com.agregator.agr;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import static com.agregator.agr.AgrApplication.DB_USERNAME;
import static com.agregator.agr.AgrApplication.DB_PASSWORD;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url("jdbc:postgresql://localhost:5432/agregator");
        dataSourceBuilder.username(DB_USERNAME);
        dataSourceBuilder.password(DB_PASSWORD);
        return dataSourceBuilder.build();
    }
}