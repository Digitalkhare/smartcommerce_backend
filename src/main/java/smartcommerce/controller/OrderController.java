package smartcommerce.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    
    @PostMapping("/place")
    public Order placeOrder(@AuthenticationPrincipal UserDetails user) {
        return orderService.placeOrder(user.getUsername());
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

}
