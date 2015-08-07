package demo.customer;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import demo.customer.domain.Customer;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration
@EnableJpaRepositories(
		entityManagerFactoryRef = "customerEntityManager",
		transactionManagerRef = "customerTransactionManager",
		basePackageClasses = Customer.class)
public class CustomerConfig {


	@Bean
	@Primary
	@ConfigurationProperties(prefix = "app.customer.datasource")
	public DataSource customerDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean customerEntityManager(
			EntityManagerFactoryBuilder builder) {
		return builder
				.dataSource(customerDataSource())
				.packages(Customer.class)
				.persistenceUnit("customers")
				.build();
	}

	@Bean
	@Primary
	public JpaTransactionManager customerTransactionManager(EntityManagerFactory customerEntityManager) {
		return new JpaTransactionManager(customerEntityManager);
	}
}
