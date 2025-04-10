package smartcommerce.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RecommendationRequest {
	@JsonProperty("user_id")
	private String userId;
	@JsonProperty("product_history")
    private List<String> productHistory;
}
