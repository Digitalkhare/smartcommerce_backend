package smartcommerce.dto;

import lombok.Data;

@Data
public class ReviewDTO {
	private String userId;
    private String productId;
    private Double rating;
    private String comment; // Add this if you want to show comment too
    private String email;   // 👈 Add this
    private String firstName;
    private String lastName;
}
