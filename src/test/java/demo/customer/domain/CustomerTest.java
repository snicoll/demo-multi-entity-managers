package demo.customer.domain;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerTest {

	@Autowired
	private CustomerRepository customerRepository;

	@Test
	public void save() {
		Customer customer = new Customer();
		customer.setFirstName("John");
		customer.setLastName("Smith");
		assertThat(customer.getId(), is(nullValue()));
		this.customerRepository.save(customer);
		assertThat(customer.getId(), is(not(nullValue())));
	}

}
