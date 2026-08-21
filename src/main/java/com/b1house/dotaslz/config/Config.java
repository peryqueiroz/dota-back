package com.b1house.dotaslz.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

@Configuration
public class Config {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        String rawUrl = System.getenv("DATABASE_URL");
        if (rawUrl == null) {
            throw new RuntimeException("DATABASE_URL não definida no ambiente");
        }
        String jdbcUrl = "jdbc:" + rawUrl;

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(jdbcUrl);
        ds.setUsername(System.getenv("DATABASE_USERNAME"));
        ds.setPassword(System.getenv("DATABASE_PASSWORD"));
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setMaximumPoolSize(10);
        return ds;
    }
}


