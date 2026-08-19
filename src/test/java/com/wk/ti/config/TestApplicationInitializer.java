package com.wk.ti.config;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestApplicationInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static final String POSTGRES_IMAGE = "postgres:17";
    private static final String DB_SCHEMA = "knowledge";
    private static final String CHANGELOG_FILE = "db/liquibase-changelog.xml";

    @SuppressWarnings("resource")
    public static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
            new PostgreSQLContainer<>(DockerImageName.parse(POSTGRES_IMAGE))
                    .withDatabaseName("postgres")
                    .withUsername("test")
                    .withPassword("test")
                    .withInitScript("db/_01-schema/_001_create_schema.sql");

    static {
        POSTGRES_CONTAINER.start();
        applyLiquibaseMigrations();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        String jdbcUrl = POSTGRES_CONTAINER.getJdbcUrl() + "?currentSchema=" + DB_SCHEMA;

        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "spring.datasource.url=" + jdbcUrl,
                "spring.datasource.username=" + POSTGRES_CONTAINER.getUsername(),
                "spring.datasource.password=" + POSTGRES_CONTAINER.getPassword(),
                "spring.liquibase.enabled=false" // Prevents Spring from re-running migrations
        );
    }

    private static void applyLiquibaseMigrations() {
        String jdbcUrl = POSTGRES_CONTAINER.getJdbcUrl() + "?currentSchema=" + DB_SCHEMA;

        try (Connection connection = DriverManager.getConnection(
                jdbcUrl,
                POSTGRES_CONTAINER.getUsername(),
                POSTGRES_CONTAINER.getPassword())) {

            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            database.setDefaultSchemaName(DB_SCHEMA);
            database.setLiquibaseSchemaName(DB_SCHEMA);

            try (Liquibase liquibase = new Liquibase(
                    CHANGELOG_FILE,
                    new ClassLoaderResourceAccessor(),
                    database)) {
                liquibase.update("");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to run manual Liquibase migration on Testcontainer", e);
        }
    }
}