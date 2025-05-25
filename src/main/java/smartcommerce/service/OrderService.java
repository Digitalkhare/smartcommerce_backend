package smartcommerce.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final MailService mailService;
    private final SimpMessagingTemplate messagingTemplate;

    public Order placeOrder(String email, String paymentIntentId) {
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
        order.setPaymentReference(paymentIntentId); // new
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
        // Format summary HTML
        String summaryHtml = items.stream()
                .map(i -> "<p>" + i.getProduct().getName() + " × " + i.getQuantity() + "</p>")
                .collect(Collectors.joining());

        // Send confirmation email
        mailService.sendOrderConfirmationHtml(
                user.getEmail(),
                user.getFirstName()+" "+user.getLastName(),
                order.getId().toString(),
                summaryHtml
        );
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
    @Transactional
    public Order updateOrderStatus(Long orderId, String newStatus) {
        //Order order = orderRepo.findById(orderId)
    	Order order = orderRepo.findByIdWithUser(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(newStatus);
        Order updatedOrder = orderRepo.save(order);

        // 🚀 Send WebSocket message to client
        messagingTemplate.convertAndSend(
            "/topic/orders/" + order.getUser().getId(), // e.g., user-specific channel
            updatedOrder
        );
       // System.err.println("Sending update to /topic/orders/" + order.getUser().getId());


        return updatedOrder;
    }



}
