package smartcommerce.dto;

import lombok.Data;

@Data
public class ReviewRequestDTO {
	private int rating;
    private String comment;
}
