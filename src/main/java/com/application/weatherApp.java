package com.application;

import com.application.data.service.SampleAddressRepository;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.sql.init.SqlDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;


@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
@Theme(value = "weatherapp", variant = Lumo.DARK)
public class weatherApp implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(weatherApp.class, args);
    }

    @Bean
    SqlDataSourceScriptDatabaseInitializer dataSourceScriptDatabaseInitializer(
            DataSource dataSource,
            SqlInitializationProperties properties,
            SampleAddressRepository sampleAddressRepository
    ) {
        // This bean ensures the database is only initialized when empty
        return new SqlDataSourceScriptDatabaseInitializer(dataSource, properties) {
            @Override
            public boolean initializeDatabase() {
                if (sampleAddressRepository.count() == 0L) {
                    return super.initializeDatabase();
                }
                return false;
            }
        };
    }
}
