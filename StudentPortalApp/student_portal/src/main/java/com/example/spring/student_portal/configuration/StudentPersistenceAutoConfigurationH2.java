package com.example.spring.student_portal.configuration;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@PropertySource({"classpath:application.properties"})
@EnableJpaRepositories(
        basePackages = "com.example.spring.student_portal.repositories.h2",
        entityManagerFactoryRef = "h2EntityManagerFactory",
        transactionManagerRef = "h2TransactionManager"
)

public class StudentPersistenceAutoConfigurationH2 {
    @Bean(name = "h2DataSource")
    @ConfigurationProperties("spring.first-datasource")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name="h2EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean h2EntityManager(EntityManagerFactoryBuilder builder, @Qualifier("h2DataSource") DataSource dataSource) {
        return builder.dataSource(dataSource).packages("com.example.spring.student_portal.models")
        .properties(Map.of(
                "hibernate.dialect", "org.hibernate.dialect.H2Dialect",
                "hibernate.hbm2ddl.auto", "update"
            )).build();
    }

    @Bean
    public PlatformTransactionManager h2TransactionManager(@Qualifier("h2EntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
