package smartcommerce.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import smartcommerce.model.Cart;
import smartcommerce.model.CartItem;
import smartcommerce.model.Order;
import smartcommerce.model.OrderItem;
import smartcommerce.model.User;
import smartcommerce.repository.CartItemRepository;
import smartcommerce.repository.CartRepository;
import smartcommerce.repository.OrderItemRepository;
import smartcommerce.repository.OrderRepository;
import smartcommerce.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final UserRepository userRepo;
    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;

    public Order placeOrder(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        Cart cart = cartRepo.findByUser(user).orElseThrow();
        List<CartItem> items = cartItemRepo.findByCart(cart);

        if (items.isEmpty()) throw new RuntimeException("Cart is empty");

        double total = items.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setTotalAmount(total);
        order.setOrderDateTime(LocalDateTime.now());
        order = orderRepo.save(order);

        for (CartItem item : items) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getProduct().getPrice());
            orderItemRepo.save(orderItem);
        }

        cartItemRepo.deleteAll(items);
        return order;
    }

    public List<Order> getUserOrders(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        return orderRepo.findByUser(user);
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }
    public Order findLatestOrderForUser(User user) {
        return orderRepo.findTopByUserOrderByOrderDateTimeDesc(user)
                .orElseThrow(() -> new RuntimeException("No recent orders found"));
    }
    public List<String> getRecentProductIds(User user) {
        List<Order> orders = orderRepo.findByUserOrderByOrderDateTimeDesc(user); // or however you define history
        return orders.stream()
            .flatMap(order -> order.getOrderItems().stream())
            .map(item -> item.getProduct().getId().toString())
            .distinct()
            .toList();
    }


}
