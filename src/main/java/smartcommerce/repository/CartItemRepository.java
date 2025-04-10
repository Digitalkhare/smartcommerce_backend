package smartcommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import smartcommerce.model.Cart;
import smartcommerce.model.CartItem;
import smartcommerce.model.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    void deleteByCartAndProduct(Cart cart, Product product);
    List<CartItem> findByCart(Cart cart);
}
