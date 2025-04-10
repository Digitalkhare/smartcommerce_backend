package smartcommerce.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import smartcommerce.dto.ReviewDTO;
import smartcommerce.dto.ReviewRequestDTO;
import smartcommerce.model.Product;
import smartcommerce.model.Review;
import smartcommerce.model.User;
import smartcommerce.repository.ProductRepository;
import smartcommerce.repository.ReviewRepository;
import smartcommerce.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ReviewService {
	private final ReviewRepository reviewRepo;
	private final ProductRepository productRepo;
	private final UserRepository userRepo;

//	public Review addReview(String email, Long productId, ReviewRequestDTO dto) {
//		User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found!"));
//		Product product = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
//
//		Review review = new Review();
//	    review.setRating(dto.getRating());
//	    review.setComments(dto.getComment());
//	    review.setUser(user);
//	    review.setProduct(product);
//	    review.setCreatedAt(LocalDateTime.now());
//	    
//		return reviewRepo.save(review);
//	}
	
	public Review addReview(String email, Long productId, ReviewRequestDTO reviewDto) {
	    User user = userRepo.findByEmail(email)
	        .orElseThrow(() -> new RuntimeException("User not found!"));
	    Product product = productRepo.findById(productId)
	        .orElseThrow(() -> new RuntimeException("Product not found"));

	    // Check if the user has already reviewed this product
	    Optional<Review> existingOpt = reviewRepo.findByUserAndProduct(user, product);

	    Review toSave;
	    if (existingOpt.isPresent()) {
	        toSave = existingOpt.get();
	        toSave.setRating(reviewDto.getRating());
	        toSave.setComments(reviewDto.getComment());
	    } else {
	        toSave = new Review();
	        toSave.setUser(user);
	        toSave.setProduct(product);
	        toSave.setRating(reviewDto.getRating());
	        toSave.setComments(reviewDto.getComment());
	        toSave.setCreatedAt(LocalDateTime.now());
	    }

	    return reviewRepo.save(toSave);
	}


	public void deleteReview(Long reviewId, String username) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        User user = userRepo.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!review.getUser().getId().equals(user.getId()) && !user.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Not authorized to delete this review");
        }

        reviewRepo.delete(review);
    }
	// ✅ Get reviews by product
    public List<ReviewDTO> getReviewsByProductId(Long productId) {
    	return reviewRepo.findByProductId(productId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    public List<ReviewDTO> getAllReviews() {
        return reviewRepo.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ReviewDTO toDTO(Review review) {
    	ReviewDTO dto = new ReviewDTO();
        dto.setUserId(String.valueOf(review.getUser().getId()));
        dto.setProductId(String.valueOf(review.getProduct().getId()));
        dto.setRating((double) review.getRating());
        dto.setComment(review.getComments()); // include comment if needed
        User user = review.getUser();
        if (user != null) {
            dto.setEmail(user.getEmail());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
        }
        return dto;
    }
}
