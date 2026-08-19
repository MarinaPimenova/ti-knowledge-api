package com.wk.ti.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class TestApplicationInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static final String POSTGRES_IMAGE = "postgres:17";
    private static final String DB_SCHEMA = "knowledge";

    @SuppressWarnings("resource")
    public static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
            new PostgreSQLContainer<>(DockerImageName.parse(POSTGRES_IMAGE))
                    .withDatabaseName("postgres")
                    .withUsername("test")
                    .withPassword("test")
                    .withInitScript("db/_01-schema/_001_create_schema.sql"); // Ensures 'knowledge' schema exists before Liquibase runs

    static {
        POSTGRES_CONTAINER.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        // Force PostgreSQL currentSchema on connection string
        String jdbcUrl = POSTGRES_CONTAINER.getJdbcUrl() + "?currentSchema=" + DB_SCHEMA;

        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "spring.datasource.url=" + jdbcUrl,
                "spring.datasource.username=" + POSTGRES_CONTAINER.getUsername(),
                "spring.datasource.password=" + POSTGRES_CONTAINER.getPassword(),
                "spring.liquibase.enabled=true"
        );
    }
}