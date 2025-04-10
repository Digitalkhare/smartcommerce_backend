package smartcommerce.controller;


import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import smartcommerce.dto.ReviewDTO;
import smartcommerce.dto.ReviewRequestDTO;
import smartcommerce.model.Review;
import smartcommerce.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

    //  Create a new review
    @PostMapping("/{productId}")
    public Review addReview(@PathVariable Long productId,
                            @RequestBody ReviewRequestDTO reviewDto,
                            @AuthenticationPrincipal UserDetails userDetails) {
        return reviewService.addReview(userDetails.getUsername(), productId, reviewDto);
    }

    //  Get all reviews for a product
    @GetMapping("/{productId}")
    public List<ReviewDTO> getReviewsByProduct(@PathVariable Long productId) {
        return reviewService.getReviewsByProductId(productId);
    }

    //  Delete a review (assuming only the author or admin can do this)
    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable Long reviewId,
                             @AuthenticationPrincipal UserDetails userDetails) {
        reviewService.deleteReview(reviewId, userDetails.getUsername());
    }
    @GetMapping
    public List<ReviewDTO> getAllReviews() {
        return reviewService.getAllReviews();
    }
}

