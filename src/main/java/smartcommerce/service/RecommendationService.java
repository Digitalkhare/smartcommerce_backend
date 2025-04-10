package smartcommerce.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import smartcommerce.dto.RecommendationRequest;
import smartcommerce.dto.RecommendationResponse;

@Service
public class RecommendationService {
	private final RestTemplate restTemplate = new RestTemplate();

    public List<String> getRecommendations(String userId, List<String> history) {
        String url = "http://localhost:8001/recommend";

        RecommendationRequest request = new RecommendationRequest();
        request.setUserId(userId);
        request.setProductHistory(history);

        ResponseEntity<RecommendationResponse> response = restTemplate.postForEntity(
            url, request, RecommendationResponse.class
        );

        return response.getBody().getRecommendedProducts();
    }
}
