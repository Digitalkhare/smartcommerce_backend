package smartcommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import smartcommerce.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
