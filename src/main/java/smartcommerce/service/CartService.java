package smartcommerce.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import smartcommerce.model.Cart;
import smartcommerce.model.CartItem;
import smartcommerce.model.Product;
import smartcommerce.model.User;
import smartcommerce.repository.CartItemRepository;
import smartcommerce.repository.CartRepository;
import smartcommerce.repository.ProductRepository;
import smartcommerce.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CartService {
	private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;

    public Cart addToCart(String email, Long productId, int quantity) {
        User user = userRepo.findByEmail(email).orElseThrow();
        Product product = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Product with ID " + productId + " not found"));

        Cart cart = cartRepo.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepo.save(newCart);
        });

        CartItem item = cartItemRepo.findByCartAndProduct(cart, product)
                .orElse(new CartItem());
        item.setCart(cart);
        item.setProduct(product);
        int newQuantity = item.getId() == null ? quantity : item.getQuantity() + quantity;
        item.setQuantity(newQuantity);
        //item.setQuantity(quantity);
        cartItemRepo.save(item);

        cart.setItems(cartItemRepo.findByCart(cart)); // Refresh items list
        return cart;
    }

    public Cart getCart(String email) {
    	User user = userRepo.findByEmail(email).orElseThrow();
        Cart cart = cartRepo.findByUser(user)
            .orElseGet(() -> new Cart(null, user, new ArrayList<>()));

        cart.setItems(cartItemRepo.findByCart(cart)); // ✅ THIS IS CRITICAL
        return cart;
    }
    @Transactional
    public void removeFromCart(String email, Long productId) {
        User user = userRepo.findByEmail(email).orElseThrow();
        Cart cart = cartRepo.findByUser(user).orElseThrow();
        Product product = productRepo.findById(productId).orElseThrow();

        cartItemRepo.deleteByCartAndProduct(cart, product);
    }

    public void clearCart(User user) {
        cartRepo.findByUser(user).ifPresent(cart -> cartItemRepo.deleteAll(cart.getItems()));
    }
}
