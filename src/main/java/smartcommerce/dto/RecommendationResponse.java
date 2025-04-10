package smartcommerce.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RecommendationResponse {
	@JsonProperty("recommended_products")
	private List<String> recommendedProducts;
}
