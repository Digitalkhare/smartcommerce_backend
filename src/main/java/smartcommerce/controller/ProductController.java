package smartcommerce.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import smartcommerce.dto.ProductDTO;
import smartcommerce.dto.RecommendationRequest;
import smartcommerce.model.Product;
import smartcommerce.model.User;
import smartcommerce.service.OrderService;
import smartcommerce.service.ProductService;
import smartcommerce.service.RecommendationService;
import smartcommerce.service.UserDetailsServiceImpl;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
	 private final ProductService productService;
	 private final RecommendationService recommendationService;
	 private final UserDetailsServiceImpl userService;
	 private final OrderService orderService;
	 
//	 @GetMapping
//	    public List<Product> getAll() {
//	        return productService.getAll();
//	    }
	 @GetMapping("/{id}")
	    public Product getById(@PathVariable Long id) {
	        return productService.getById(id);
	    }
	 @GetMapping("/category/{categoryId}")
	    public List<Product> getByCategory(@PathVariable Long categoryId) {
	        return productService.getByCategoryId(categoryId);
	    }
	 @PreAuthorize("hasRole('ADMIN')")
	 @GetMapping("/admin")
	 public List<Product> getAllAdminProducts() {
	     return productService.getAll();
	 }
	 @PreAuthorize("hasRole('ADMIN')")
	 @PostMapping("/admin")
	    public Product create(@RequestBody Product product) {
	        return productService.create(product);
	    }
	 @PreAuthorize("hasRole('ADMIN')")
	 @PutMapping("/admin/{id}")
	    public Product update(@PathVariable Long id, @RequestBody Product product) {
	        return productService.update(id, product);
	    }
	 @PreAuthorize("hasRole('ADMIN')")
	 @DeleteMapping("/admin/{id}")
	    public void delete(@PathVariable Long id) {
	        productService.delete(id);
	    }
//	 @PostMapping("/recommended")
//	    public List<String> getRecommended(@RequestBody RecommendationRequest request) {
//		 System.err.println(">>> Received userId: " + request.getUserId());
//		    System.err.println(">>> Product history: " + request.getProductHistory());
//	        return recommendationService.getRecommendations(request.getUserId(), request.getProductHistory());
//	    }
	 @PreAuthorize("isAuthenticated()")
	 @GetMapping("/recommended")
	 public List<Product> getRecommended(@AuthenticationPrincipal UserDetails userDetails) {
		 if (userDetails == null) {
		        throw new RuntimeException("Unauthorized: No user info found");
		    }
		 User user = userService.findByEmail(userDetails.getUsername());

	     List<String> history = orderService.getRecentProductIds(user);
	     List<String> recommendedIds = recommendationService.getRecommendations(user.getId().toString(), history);

	     return productService.getByIds(recommendedIds);
	 }

	 @GetMapping("/featured")
	    public ResponseEntity<List<Product>> getFeaturedProducts() {
	        return ResponseEntity.ok(productService.getFeaturedProducts());
	    }
//	 @GetMapping
//	 public ResponseEntity<List<Product>> getAllProducts(
//	         @RequestParam(required = false) String category) {
//		// System.err.println(">>> category param: " + category);
//		 if (category != null && !category.isBlank()) {
//	         return ResponseEntity.ok(productService.getByCategory(category));
//	     }
//	     return ResponseEntity.ok(productService.getAll());
//	 }
	 @GetMapping
	 public ResponseEntity<List<ProductDTO>> getFilteredProducts(
	     @RequestParam(required = false) String category,
	     @RequestParam(required = false) List<String> subCategories,
	     @RequestParam(required = false) String search
	 )
 {

	     List<ProductDTO> products = productService.getProductsByCategoryAndSubCategories(category, subCategories, search);
	     return ResponseEntity.ok(products);
	 }


}
