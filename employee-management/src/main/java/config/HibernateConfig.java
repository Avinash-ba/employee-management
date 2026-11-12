
package com.example.employeemanagement.config;

import com.example.employeemanagement.entity.Employee;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.Properties;

@org.springframework.context.annotation.Configuration
public class HibernateConfig {

    @Bean(destroyMethod = "close")
    public SessionFactory sessionFactory(Environment environment) {

        Properties properties = new Properties();

        // Read database settings from the active Spring profile
        properties.setProperty(
                "hibernate.connection.driver_class",
                environment.getRequiredProperty(
                        "spring.datasource.driver-class-name"
                )
        );

        properties.setProperty(
                "hibernate.connection.url",
                environment.getRequiredProperty(
                        "spring.datasource.url"
                )
        );

        properties.setProperty(
                "hibernate.connection.username",
                environment.getRequiredProperty(
                        "spring.datasource.username"
                )
        );

        properties.setProperty(
                "hibernate.connection.password",
                environment.getProperty(
                        "spring.datasource.password", ""
                )
        );

        // Hibernate settings
        properties.setProperty(
                "hibernate.hbm2ddl.auto",
                environment.getProperty(
                        "spring.jpa.hibernate.ddl-auto", "update"
                )
        );

        properties.setProperty(
                "hibernate.show_sql",
                environment.getProperty(
                        "spring.jpa.show-sql", "false"
                )
        );

        properties.setProperty("hibernate.format_sql", "true");

        // Create Hibernate SessionFactory
        Configuration configuration = new Configuration();

        configuration.setProperties(properties);

        configuration.addAnnotatedClass(Employee.class);

        return configuration.buildSessionFactory();
    }
}
