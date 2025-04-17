package smartcommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import smartcommerce.model.Order;
import smartcommerce.model.User;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUser(User user);
	Optional<Order> findTopByUserOrderByOrderDateTimeDesc(User user);
	List<Order> findByUserOrderByOrderDateTimeDesc(User user);
	


}
