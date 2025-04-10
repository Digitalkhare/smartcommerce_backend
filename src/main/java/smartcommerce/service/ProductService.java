package smartcommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import smartcommerce.model.Product;
import smartcommerce.model.Review;
import smartcommerce.repository.ProductRepository;
import smartcommerce.repository.ReviewRepository;

@Service
@RequiredArgsConstructor
public class ProductService {
	final ProductRepository productRepository;
	final ReviewRepository reviewRepo;

	public List<Product> getAll() {
		return productRepository.findAll();
	}

	public Product getById(Long id) {
		return productRepository.findById(id).orElseThrow();
	}

	public List<Product> getByCategoryId(Long categoryId) {
		return productRepository.findByCategoryId(categoryId);
	}

	public Product create(Product product) {
		return productRepository.save(product);
	}

	public Product update(Long id, Product product) {
		Product existing = productRepository.findById(id).orElseThrow();
		existing.setName(product.getName());
		existing.setPrice(product.getPrice());
		existing.setDescription(product.getDescription());
		existing.setStock(product.getStock());
		existing.setImageUrl(product.getImageUrl());
		existing.setCategory(product.getCategory());
		return productRepository.save(existing);
	}

	public void delete(Long id) {
		productRepository.deleteById(id);
	}
	public double getAverageRating(Long productId) {
	    List<Review> reviews = reviewRepo.findByProductId(productId);
	    return reviews.stream()
	        .mapToInt(Review::getRating)
	        .average()
	        .orElse(0.0);
	}
	public List<Product> getFeaturedProducts() {
        return productRepository.findByFeaturedTrue();
    }
	public List<Product> getByCategory(String category) {
	    return productRepository.findByCategory_NameIgnoreCase(category);
	}
	public List<Product> getByIds(List<String> ids) {
	    return productRepository.findAllById(
	        ids.stream().map(Long::valueOf).toList()
	    );
	}

}
