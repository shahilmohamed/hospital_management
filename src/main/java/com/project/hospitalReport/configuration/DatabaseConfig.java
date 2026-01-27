package com.project.hospitalReport.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DatabaseConfig implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String mysqlPublicUrl = environment.getProperty("MYSQL_PUBLIC_URL");
        
        if (mysqlPublicUrl != null && !mysqlPublicUrl.isEmpty()) {
            try {
                // Parse the Railway URL format: mysql://user:password@host:port/database
                // Convert to JDBC format: jdbc:mysql://host:port/database?params
                
                // Handle the mysql:// prefix
                String url = mysqlPublicUrl;
                if (url.startsWith("mysql://")) {
                    url = "http://" + url.substring(8); // Replace mysql:// with http:// for URI parsing
                }
                
                URI uri = new URI(url);
                String host = uri.getHost();
                int port = uri.getPort() == -1 ? 3306 : uri.getPort();
                String database = uri.getPath();
                if (database.startsWith("/")) {
                    database = database.substring(1);
                }
                
                // Extract username and password from userInfo
                String userInfo = uri.getUserInfo();
                String username = null;
                String password = null;
                if (userInfo != null && userInfo.contains(":")) {
                    String[] credentials = userInfo.split(":", 2);
                    username = URLDecoder.decode(credentials[0], StandardCharsets.UTF_8);
                    password = URLDecoder.decode(credentials[1], StandardCharsets.UTF_8);
                }
                
                // Build JDBC URL
                String jdbcUrl = String.format(
                    "jdbc:mysql://%s:%d/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                    host, port, database
                );
                
                // Set the properties
                Map<String, Object> properties = new HashMap<>();
                properties.put("spring.datasource.url", jdbcUrl);
                
                // Set username and password if extracted from URL
                if (username != null) {
                    properties.put("spring.datasource.username", username);
                }
                if (password != null) {
                    properties.put("spring.datasource.password", password);
                }
                
                // Add the property source with highest precedence
                environment.getPropertySources().addFirst(
                    new MapPropertySource("railwayDatabaseConfig", properties)
                );
                
            } catch (Exception e) {
                System.err.println("Error parsing MYSQL_PUBLIC_URL: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}

