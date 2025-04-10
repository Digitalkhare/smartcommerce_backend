package smartcommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import smartcommerce.model.Product;
import smartcommerce.model.Review;
import smartcommerce.model.User;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	List<Review> findByProductId(Long productId);
	Optional<Review> findByUserAndProduct(User user, Product product);

}

