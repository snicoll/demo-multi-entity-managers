package demo.customer.domain;

import demo.DemoMultiEntityManagers;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoMultiEntityManagers.class)
public class CustomerTest {

	@Autowired
	private CustomerRepository customerRepository;

	@Test
	@Transactional
	public void save() {
		Customer customer = new Customer();
		customer.setFirstName("John");
		customer.setLastName("Smith");
		assertThat(customer.getId(), is(nullValue()));
		this.customerRepository.save(customer);
		assertThat(customer.getId(), is(not(nullValue())));
	}

}
