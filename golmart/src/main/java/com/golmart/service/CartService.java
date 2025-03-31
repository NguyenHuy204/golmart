package com.golmart.service;

import com.golmart.dto.CartDTO;
import com.golmart.entity.Account;
import com.golmart.entity.Cart;
import com.golmart.entity.CartItem;
import com.golmart.entity.Product;
import com.golmart.repository.CartRepository;
import com.golmart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public CartDTO getOrCreateCart(Account account) {
        Cart cart = findOrCreateCart(account);
        return convertToDTO(cart);
    }

    @Transactional
    public CartDTO addToCart(Long accountId, Long productId, int quantity) {
        Cart cart = findCartById(accountId);
        Product product = findProductById(productId);

        if (product.getStock() < quantity) {
            throw new IllegalStateException("Insufficient stock");
        }

        cart.addItem(product, quantity);
        return convertToDTO(cartRepository.save(cart));
    }

    @Transactional
    public CartDTO updateCartItemQuantity(Long accountId, Long productId, int quantity) {
        Cart cart = findCartById(accountId);
        Product product = findProductById(productId);

        if (product.getStock() < quantity) {
            throw new IllegalStateException("Insufficient stock");
        }

        cart.updateItemQuantity(productId, quantity);
        return convertToDTO(cartRepository.save(cart));
    }

    @Transactional
    public CartDTO removeFromCart(Long accountId, Long productId) {
        Cart cart = findCartById(accountId);
        cart.removeItem(productId);
        return convertToDTO(cartRepository.save(cart));
    }

    @Transactional
    public void clearCart(Long accountId) {
        Cart cart = findCartById(accountId);
        cart.clear();
        cartRepository.save(cart);
    }

    @Transactional(readOnly = true)
    public CartDTO getCartDTO(Long accountId) {
        Cart cart = findCartById(accountId);
        return convertToDTO(cart);
    }

    @Transactional(readOnly = true)
    protected Cart findCartById(Long accountId) {
        return cartRepository.findByAccountId(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
    }

    private Cart findOrCreateCart(Account account) {
        return cartRepository.findByAccountId(account.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setAccount(account);
                    return cartRepository.save(newCart);
                });
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    private CartDTO convertToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setTotalAmount(cart.getTotalAmount());
        dto.setTotalItems(cart.getItems().size());
        dto.setItems(cart.getItems().stream()
                .map(this::convertToItemDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    private CartDTO.CartItemDTO convertToItemDTO(CartItem item) {
        CartDTO.CartItemDTO dto = new CartDTO.CartItemDTO();
        dto.setId(item.getId());
        dto.setQuantity(item.getQuantity());
        dto.setSubtotal(item.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity())));
        dto.setProduct(convertToProductDTO(item.getProduct()));
        return dto;
    }

    private CartDTO.ProductDTO convertToProductDTO(Product product) {
        CartDTO.ProductDTO dto = new CartDTO.ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setImageUrl(product.getImageUrl());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        return dto;
    }
} 