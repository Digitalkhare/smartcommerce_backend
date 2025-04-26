package smartcommerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import smartcommerce.dto.ProductDTO;
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

//	public void delete(Long id) {
//		productRepository.deleteById(id);
//	}
	public void delete(Long id) {
	    try {
	        productRepository.deleteById(id);
	    } catch (EmptyResultDataAccessException e) {
	        throw new RuntimeException("Product not found with id: " + id);
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to delete product: " + e.getMessage());
	    }
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
	public List<ProductDTO> getProductsByCategoryAndSubCategories(String category, List<String> subCategories, String search) {
	    List<Product> products;

	    if (category != null && !category.equalsIgnoreCase("All")) {
	        if (subCategories != null && !subCategories.isEmpty()) {
	            products = productRepository.findByCategory_NameAndSubCategory_NameIn(category, subCategories);
	        } else {
	            products = productRepository.findByCategory_NameIgnoreCase(category);
	        }
	    } else {
	        products = productRepository.findAll(); // 💥 Default when category is missing
	    }

	    if (search != null && !search.trim().isEmpty()) {
	        String searchTerm = search.trim().toLowerCase();
	        products = products.stream()
	            .filter(p -> p.getName().toLowerCase().contains(searchTerm) ||
	                         p.getDescription().toLowerCase().contains(searchTerm))
	            .collect(Collectors.toList());
	    }

	    return products.stream().map(this::mapToDTO).collect(Collectors.toList());
	}



    public ProductDTO mapToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setImageUrl(product.getImageUrl());
        dto.setCategory(product.getCategory().getName());
        dto.setSubCategory(
                product.getSubCategory() != null ? product.getSubCategory().getName() : null
            );
        return dto;
    }

}
