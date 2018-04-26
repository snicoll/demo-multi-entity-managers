package demo.customer.domain;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest // This slice test uses only enables selected auto-configuration and ignores CustomerConfig
public class CustomerTest {

	@Autowired
	private CustomerRepository customerRepository;

	@Test
	public void save() {
		Customer customer = new Customer("John", "Smith");
		assertThat(customer.getId()).isNull();
		this.customerRepository.save(customer);
		assertThat(customer.getId()).isNotNull();
	}

}
