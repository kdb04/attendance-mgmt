package com.AttendanceManagementSystem.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

public class DBConnection {
    private static DBConnection instance;
    private JdbcTemplate jdbcTemplate;

    // Private constructor to prevent direct instantiation
    private DBConnection() {
        DataSource dataSource = createDataSource();
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Synchronized getInstance method to ensure thread safety
    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    private DataSource createDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        String host = System.getenv("DB_HOST");
        String port = System.getenv("DB_PORT");
        String dbName = System.getenv("DB_NAME");
        String username = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://" + host + ":" + port + "/" + dbName);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
