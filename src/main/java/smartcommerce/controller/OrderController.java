package smartcommerce.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import smartcommerce.model.Order;
import smartcommerce.model.User;
import smartcommerce.service.OrderService;
import smartcommerce.service.UserDetailsServiceImpl;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;
	private final UserDetailsServiceImpl userService;

	// ✅ Updated to accept paymentIntentId from frontend
	@PostMapping("/place")
	public ResponseEntity<Order> placeOrder(@AuthenticationPrincipal UserDetails user,
			@RequestBody(required = false) Map<String, String> body) {
		String paymentIntentId = body != null ? body.getOrDefault("paymentIntentId", null) : null;
		Order order = orderService.placeOrder(user.getUsername(), paymentIntentId);
		return ResponseEntity.ok(order);
	}

	@GetMapping
	public List<Order> userOrders(@AuthenticationPrincipal UserDetails user) {
		return orderService.getUserOrders(user.getUsername());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin")
	public List<Order> allOrders() {
		return orderService.getAllOrders();
	}

	@GetMapping("/latest")
	public ResponseEntity<Order> getLatestOrder(Principal principal) {
		User user = userService.findByEmail(principal.getName());
		Order order = orderService.findLatestOrderForUser(user);
		return ResponseEntity.ok(order);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{orderId}/status")
	public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId, @RequestBody Map<String, String> body) {
		 //System.err.println("📩 Admin triggered status update for order " + orderId); // 👈 ADD THIS
		String newStatus = body.get("status");
		Order updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
		return ResponseEntity.ok(updatedOrder);
	}
}
