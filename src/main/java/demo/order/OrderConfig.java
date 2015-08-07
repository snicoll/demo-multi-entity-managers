package demo.order;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import demo.order.domain.Order;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration
@EnableJpaRepositories(
		entityManagerFactoryRef = "orderEntityManager",
		transactionManagerRef = "orderTransactionManager",
		basePackageClasses = Order.class)
public class OrderConfig {

	@Bean
	@ConfigurationProperties(prefix = "app.order.datasource")
	public DataSource orderDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean orderEntityManager(
			EntityManagerFactoryBuilder builder) {
		return builder
				.dataSource(orderDataSource())
				.packages(Order.class)
				.persistenceUnit("orders")
				.build();
	}

	@Bean
	public JpaTransactionManager orderTransactionManager(EntityManagerFactory orderEntityManager) {
		return new JpaTransactionManager(orderEntityManager);
	}

}
