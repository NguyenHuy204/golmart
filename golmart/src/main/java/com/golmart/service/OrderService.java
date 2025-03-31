package com.golmart.service;

import com.golmart.dto.OrderDTO;
import com.golmart.dto.ProductDTO;
import com.golmart.entity.*;
import com.golmart.repository.OrderRepository;
import com.golmart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    @Transactional
    public OrderDTO createOrder(Long accountId, OrderDTO.CheckoutRequest checkoutRequest) {
        // Get cart entity directly from CartService's protected method
        Cart cart = cartService.findCartById(accountId);
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        Order order = new Order();
        order.setAccount(cart.getAccount());
        order.setShippingAddress(checkoutRequest.getShippingAddress());
        order.setPhone(checkoutRequest.getPhone());
        order.setNote(checkoutRequest.getNote());

        // Transfer items from cart to order
        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for product: " + product.getName());
            }
            
            // Update product stock
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            // Add item to order
            order.addItem(product, cartItem.getQuantity());
        }

        order.calculateTotal();
        Order savedOrder = orderRepository.save(order);

        // Clear the cart after successful order creation
        cartService.clearCart(accountId);

        return convertToDTO(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByAccount(Long accountId) {
        return orderRepository.findByAccountIdOrderByCreatedAtDesc(accountId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderDTO getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return convertToDTO(order);
    }

    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.setStatus(status);
        return convertToDTO(orderRepository.save(order));
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setAccountId(order.getAccount().getId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setPhone(order.getPhone());
        dto.setNote(order.getNote());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        
        dto.setItems(order.getItems().stream()
                .map(this::convertToItemDTO)
                .collect(Collectors.toList()));
        
        return dto;
    }

    private OrderDTO.OrderItemDTO convertToItemDTO(OrderItem item) {
        OrderDTO.OrderItemDTO dto = new OrderDTO.OrderItemDTO();
        dto.setId(item.getId());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setSubtotal(item.getSubtotal());
        dto.setProduct(convertToProductDTO(item.getProduct()));
        return dto;
    }

    private ProductDTO convertToProductDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setImageUrl(product.getImageUrl());
        dto.setCategoryId(product.getCategory().getId());
        return dto;
    }
} 