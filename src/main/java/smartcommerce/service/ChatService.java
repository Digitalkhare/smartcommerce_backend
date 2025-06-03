package smartcommerce.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import smartcommerce.model.Order;
import smartcommerce.model.OrderItem;
import smartcommerce.model.Product;
import smartcommerce.model.User;
import smartcommerce.repository.OrderRepository;
import smartcommerce.repository.ProductRepository;
import smartcommerce.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepo;
    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;
    private final RecommendationService recommendationService;
    private final OrderService orderService;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;
    boolean greetings;

    public String getSmartResponse(String email, String userMessage) {
        User user = userRepo.findByEmail(email).orElseThrow();
         String firstName = user.getFirstName();
         String lastName = user.getLastName();

        Order recentOrder = orderRepo.findTopByUserOrderByOrderDateTimeDesc(user).orElse(null);
        //List<Product> recommended = recommendationService.getRecommendations(user.getId());
        
         List<String> history = orderService.getRecentProductIds(user);
         
         List<String> recommendedIds;
         try {
             recommendedIds = recommendationService.getRecommendations(user.getId().toString(), history);
         } catch (Exception e) {
             System.err.println("⚠️ Recommendation engine unreachable: " + e.getMessage());
             recommendedIds = new ArrayList<>();
         }
     
	     List<Long> recommendedLongIds = recommendedIds.stream()
	    		    .map(Long::valueOf)
	    		    .collect(Collectors.toList());

	     List<Product> recommended = productRepo.findAllById(recommendedLongIds);
	     

        // Fallback to featured products
        if (recommendedIds == null || recommendedIds.isEmpty()) {
        	recommended = productRepo.findByFeaturedTrue().stream().limit(5).collect(Collectors.toList());;
        }

        StringBuilder systemPrompt = new StringBuilder();
        systemPrompt.append("You are a helpful eCommerce assistant. ");
        if (!greetings) {
        	 
        	systemPrompt.append("if user has not been greeted").append(greetings).append("Greet the user by first and last names. Their name are, ").append(firstName).append(" ").append(lastName).append(". ");
		      greetings = true;
        } else {
			 systemPrompt.append("if user has been greeted").append(greetings).append("donot greet user again");
		}
       // systemPrompt.append("Greet the user by first and last names. Their name are, ").append(firstName).append(" ").append(lastName).append(". ");
        systemPrompt.append("Introduce yourself as Jessica ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a");
        if (recentOrder != null) {
            systemPrompt.append("Their most recent order was on ")
                        .append(recentOrder.getOrderDateTime().format(formatter)).append(" and includes: ");
            for (OrderItem item : recentOrder.getOrderItems()) {
                systemPrompt.append(item.getProduct().getName()).append(" (x").append(item.getQuantity()).append("), ");
            }
        } else {
            systemPrompt.append("They haven't placed an order yet. ");
        }
         
        systemPrompt.append("Recommended products: ");
        for (Product p : recommended) {
            systemPrompt.append(p.getName()).append(", ");
        }

       // systemPrompt.append("Answer any product or order-related question in a friendly tone.");
        systemPrompt.append("You can answer questions about orders, products, availability, returns, and promotions in a friendly and helpful tone.");
        systemPrompt.append("\n");

        // Behaviours and instructions for a wide range of support cases
        systemPrompt.append("""
            Behaviour rules for SmartBot:
            - Answer questions about product availability, categories, descriptions, and comparisons.
            - Help users track or understand their recent orders.
            - Explain our return policy (7-day window, original packaging, auto refund in 5 days).
            - Explain delivery and shipping timelines (3–5 days).
            - Let users know about current deals, featured products, or discounts if asked.
            - For account questions, direct them to the "Account" section of the website.
            - If the user asks for live support, politely confirm and tell them someone will reach out.
            - Offer to help with anything else after each response.

            Example Scenarios:
            - "Where’s my last order?" → Mention their last order and items included.
            - "Do you sell [product]?" → Respond with yes/no and show related items if yes.
            - "I want to return something" → Walk through the return policy.
            - "Any offers this week?" → Mention current promotions or featured deals.
            - "What’s in my cart?" → Explain how to check cart (you don't access it directly).
            - "I want to reorder" → Confirm and link back to last items if possible.
            - "How long is shipping?" → 3–5 business days.
            - "Can I talk to support?" → Offer live support handoff.

            Never guess product prices or delivery tracking numbers unless included in user context.
            Do not hallucinate product names that don’t exist.
            Stay short and helpful. Encourage browsing or ordering if possible.
        """);
        return sendToGpt(systemPrompt.toString(), userMessage);
    }

    private String sendToGpt(String systemPrompt, String userMessage) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> request = new HashMap<>();
        request.put("model", "gpt-3.5-turbo");
        request.put("messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userMessage)
        ));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            return message.get("content").toString();
        } catch (Exception e) {
        	e.printStackTrace();
            return "Sorry, I'm having trouble accessing your data right now.";
        }
    }
}
