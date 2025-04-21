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
		dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
		dataSource.setUrl("jdbc:mariadb://localhost:3306/ooad");
        dataSource.setUsername("vorrtt3x");
        dataSource.setPassword("1234");
        return dataSource;
    }
    
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
