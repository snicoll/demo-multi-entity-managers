package demo.customer;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import demo.customer.domain.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@EnableJpaRepositories(
		entityManagerFactoryRef = "customerEntityManager",
		transactionManagerRef = "customerTransactionManager",
		basePackageClasses = Customer.class)
public class CustomerConfig {

	@Autowired(required = false)
	private PersistenceUnitManager persistenceUnitManager;

	@Bean
	@ConfigurationProperties("app.customer.jpa")
	public JpaProperties customerJpaProperties() {
		return new JpaProperties();
	}

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "app.customer.datasource")
	public DataSource customerDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean customerEntityManager(
			JpaProperties customerJpaProperties) {
		EntityManagerFactoryBuilder builder = createEntityManagerFactoryBuilder(customerJpaProperties);
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

	private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(JpaProperties customerJpaProperties) {
		JpaVendorAdapter jpaVendorAdapter = createJpaVendorAdapter(customerJpaProperties);
		return new EntityManagerFactoryBuilder(
				jpaVendorAdapter, customerJpaProperties, this.persistenceUnitManager);
	}

	private JpaVendorAdapter createJpaVendorAdapter(JpaProperties jpaProperties) {
		AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setShowSql(jpaProperties.isShowSql());
		adapter.setDatabase(jpaProperties.getDatabase());
		adapter.setDatabasePlatform(jpaProperties.getDatabasePlatform());
		adapter.setGenerateDdl(jpaProperties.isGenerateDdl());
		return adapter;
	}

}
