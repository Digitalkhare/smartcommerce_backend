package smartcommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import smartcommerce.model.Cart;
import smartcommerce.model.User;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Optional<Cart> findByUser(User user);
    void deleteByUser(User user);
}
