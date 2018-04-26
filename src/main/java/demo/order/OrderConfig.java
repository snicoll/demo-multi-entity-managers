package demo.order;

import javax.persistence.EntityManagerFactory;

import demo.order.domain.Order;
import org.apache.tomcat.jdbc.pool.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@EnableJpaRepositories(
		entityManagerFactoryRef = "orderEntityManager",
		transactionManagerRef = "orderTransactionManager",
		basePackageClasses = Order.class)
public class OrderConfig {

	@Autowired(required = false)
	private PersistenceUnitManager persistenceUnitManager;

	@Bean
	@ConfigurationProperties("app.order.jpa")
	public JpaProperties orderJpaProperties() {
		return new JpaProperties();
	}

	@Bean
	@ConfigurationProperties(prefix = "app.order.datasource")
	public DataSource orderDataSource() {
		return (DataSource) DataSourceBuilder.create().type(DataSource.class).build();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean orderEntityManager(
			JpaProperties orderJpaProperties) {
		EntityManagerFactoryBuilder builder = createEntityManagerFactoryBuilder(orderJpaProperties);
		return builder
				.dataSource(orderDataSource())
				.packages(Order.class)
				.persistenceUnit("ordersDs")
				.build();
	}

	@Bean
	public JpaTransactionManager orderTransactionManager(EntityManagerFactory orderEntityManager) {
		return new JpaTransactionManager(orderEntityManager);
	}

	private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(JpaProperties orderJpaProperties) {
		JpaVendorAdapter jpaVendorAdapter = createJpaVendorAdapter(orderJpaProperties);
		return new EntityManagerFactoryBuilder(jpaVendorAdapter,
				orderJpaProperties.getProperties(), this.persistenceUnitManager);
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
