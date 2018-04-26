package demo.order.domain;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest // This slice test uses only enables selected auto-configuration and ignores OrderConfig
public class OrderTest {

	@Autowired
	private OrderRepository orderRepository;

	@Test
	public void save() {
		Order order = new Order(123L, new Date());
		assertThat(order.getId()).isNull();
		this.orderRepository.save(order);
		assertThat(order.getId()).isNotNull();
	}

}
