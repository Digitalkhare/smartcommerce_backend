package smartcommerce.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import smartcommerce.model.Cart;
import smartcommerce.service.CartService;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
	private final CartService cartService;

    @PostMapping("/add")
    public Cart addToCart(@AuthenticationPrincipal UserDetails user,
                          @RequestParam Long productId,
                          @RequestParam int quantity) {
        return cartService.addToCart(user.getUsername(), productId, quantity);
    }

    @GetMapping
    public Cart getCart(@AuthenticationPrincipal UserDetails user) {
        return cartService.getCart(user.getUsername());
    }

    @DeleteMapping("/remove")
    public void remove(@AuthenticationPrincipal UserDetails user,
                       @RequestParam Long productId) {
        cartService.removeFromCart(user.getUsername(), productId);
    }
}
