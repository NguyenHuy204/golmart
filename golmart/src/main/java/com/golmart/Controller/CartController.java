package com.golmart.Controller;

import com.golmart.dto.CartDTO;
import com.golmart.service.CartService;
import com.golmart.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartDTO> addToCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody Map<String, Object> request) {
        Long productId = Long.parseLong(request.get("productId").toString());
        int quantity = Integer.parseInt(request.get("quantity").toString());
        CartDTO updatedCart = cartService.addToCart(userDetails.getId(), productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @PutMapping("/update")
    public ResponseEntity<CartDTO> updateCartItem(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody Map<String, Object> request) {
        Long productId = Long.parseLong(request.get("productId").toString());
        int quantity = Integer.parseInt(request.get("quantity").toString());
        CartDTO updatedCart = cartService.updateCartItemQuantity(userDetails.getId(), productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<CartDTO> removeFromCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long productId) {
        CartDTO updatedCart = cartService.removeFromCart(userDetails.getId(), productId);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        cartService.clearCart(userDetails.getId());
        return ResponseEntity.ok().build();
    }

//    @GetMapping
//    public ResponseEntity<CartDTO> getCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
////        CartDTO cart = cartService.getCart(userDetails.getId());
//        return ResponseEntity.ok(cart);
//    }
} 