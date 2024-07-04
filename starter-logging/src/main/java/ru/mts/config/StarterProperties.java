package ru.mts.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "starter-logging")
public class StarterProperties {
    private String author;
}
