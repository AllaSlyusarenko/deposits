package ru.mts;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.springframework.boot.test.context.TestConfiguration;

import javax.sql.DataSource;

@TestConfiguration
public class TestDatabaseConfig {
    private static final String POSTGRES_IMAGE = "postgres";

    @Bean(initMethod = "start", destroyMethod = "stop")
    PostgreSQLContainer<?> postgresContainer(DockerImageName dockerPostgres) {
        return new PostgreSQLContainer<>(dockerPostgres)
                .withDatabaseName("postgres")
                .waitingFor(Wait.forListeningPort());
    }

    @Bean
    DockerImageName dockerPostgres() {
        return DockerImageName.parse(POSTGRES_IMAGE);
    }

    @Bean
    public DataSource dataSource(PostgreSQLContainer<?> postgresContainer) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setUsername(postgresContainer.getUsername());
        hikariConfig.setPassword(postgresContainer.getPassword());
        hikariConfig.setJdbcUrl(configureSpyUrl(postgresContainer));

        return new HikariDataSource(hikariConfig);
    }

    private static String configureSpyUrl(PostgreSQLContainer<?> postgresContainer) {
        return "jdbc:postgresql://"
                + postgresContainer.getHost()
                + ":"
                + postgresContainer.getMappedPort(5432)
                + "/"
                + postgresContainer.getDatabaseName();
    }
}
