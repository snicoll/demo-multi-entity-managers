package demo.order.domain;

import java.util.Date;

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
public class OrderTest {

	@Autowired
	private OrderRepository orderRepository;

	@Test
	@Transactional
	public void save() {
		Order order = new Order();
		order.setCustomerId(123L);
		order.setOrderDate(new Date());
		assertThat(order.getId(), is(nullValue()));
		this.orderRepository.save(order);
		assertThat(order.getId(), is(not(nullValue())));
	}

}
