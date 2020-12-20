package demo;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Date;

import javax.sql.DataSource;

import demo.customer.domain.Customer;
import demo.customer.domain.CustomerRepository;
import demo.order.domain.Order;
import demo.order.domain.OrderRepository;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	@Qualifier("customerDataSource")
	private DataSource customerDataSource;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	@Qualifier("orderDataSource")
	private DataSource orderDataSource;

	@Autowired
	private OrderRepository orderRepository;

	@Test
	void customerDataSourceIsInitializedProperly() {
		JdbcTemplate customerTemplate = new JdbcTemplate(this.customerDataSource);
		assertThat(hasTable(customerTemplate, "CUSTOMER")).isTrue();
		assertThat(hasTable(customerTemplate, "CUSTOMER_ORDER")).isFalse();
	}

	@Test
	void customerRepositoryConfiguredProperly() {
		JdbcTemplate customerTemplate = new JdbcTemplate(this.customerDataSource);
		int count = countItem(customerTemplate, "CUSTOMER");
		Customer customer = new Customer("John", "Smith");
		this.customerRepository.save(customer);
		assertThat(countItem(customerTemplate, "CUSTOMER")).isEqualTo(++count);
	}

	@Test
	void orderDataSourceIsInitializedProperly() {
		JdbcTemplate orderTemplate = new JdbcTemplate(this.orderDataSource);
		assertThat(hasTable(orderTemplate, "CUSTOMER_ORDER")).isTrue();
		assertThat(hasTable(orderTemplate, "CUSTOMER")).isFalse();
	}

	@Test
	void orderRepositoryConfiguredProperly() {
		JdbcTemplate orderTemplate = new JdbcTemplate(this.orderDataSource);
		int count = countItem(orderTemplate, "CUSTOMER_ORDER");
		Order order = new Order(123L, new Date());
		this.orderRepository.save(order);
		assertThat(countItem(orderTemplate, "CUSTOMER_ORDER")).isEqualTo(++count);
	}

	private boolean hasTable(JdbcTemplate jdbcTemplate, String tableName) {
		return jdbcTemplate.execute((ConnectionCallback<Boolean>) connection -> {
			DatabaseMetaData md = connection.getMetaData();
			try (ResultSet rs = md.getTables(null, null, tableName, new String[] { "TABLE" })) {
				return (rs.next());
			}
		});
	}

	private int countItem(JdbcTemplate jdbcTemplate, String tableName) {
		return jdbcTemplate.queryForObject(String.format("SELECT COUNT(*) from %s", tableName), Integer.class);
	}

}
