package com.example.spring.student_portal.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@PropertySource({"classpath:application.properties"})
@EnableJpaRepositories(
        basePackages = "com.example.spring.student_portal.repositories.postgres",
        entityManagerFactoryRef = "postgresEntityManagerFactory",
        transactionManagerRef = "postgresTransactionManager"
)
public class StudentPersistenceAutoConfigurationPostgres {
    @Bean(name = "postgresDataSource")
    @Primary
    @ConfigurationProperties("spring.second-datasource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name="postgresEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean  postgresEntityManager(EntityManagerFactoryBuilder builder, @Qualifier("postgresDataSource") DataSource dataSource) {
        return builder.dataSource(dataSource).packages("com.example.spring.student_portal.models").build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager postgresTransactionManager(@Qualifier("postgresEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
 