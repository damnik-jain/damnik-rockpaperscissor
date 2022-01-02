package com.hackerearth.fungame.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    DataSource dataSource(){
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.h2.Driver");
        dataSourceBuilder.url("jdbc:h2:file:./data/db");
        dataSourceBuilder.username("sa");
        dataSourceBuilder.password("password");
        return dataSourceBuilder.build();
    }

    @Bean
    JdbcTemplate jdbcTemplate(@Qualifier("dataSource") DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean
    NamedParameterJdbcTemplate namedParameterJdbcTemplate(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate){
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }

}
