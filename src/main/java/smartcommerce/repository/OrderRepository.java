package smartcommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import smartcommerce.model.Order;
import smartcommerce.model.User;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUser(User user);
	Optional<Order> findTopByUserOrderByOrderDateTimeDesc(User user);
	List<Order> findByUserOrderByOrderDateTimeDesc(User user);
	@Query("SELECT o FROM Order o JOIN FETCH o.user WHERE o.id = :orderId")
	Optional<Order> findByIdWithUser(@Param("orderId") Long orderId);

	


}
