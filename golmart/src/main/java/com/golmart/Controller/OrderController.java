package com.golmart.Controller;

import com.golmart.dto.OrderDTO;
import com.golmart.entity.OrderStatus;
import com.golmart.security.UserDetailsImpl;
import com.golmart.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<OrderDTO> checkout(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody OrderDTO.CheckoutRequest request) {
        OrderDTO order = orderService.createOrder(userDetails.getId(), request);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getMyOrders(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<OrderDTO> orders = orderService.getOrdersByAccount(userDetails.getId());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long orderId) {
        OrderDTO order = orderService.getOrder(orderId);
        if (!order.getAccountId().equals(userDetails.getId()) && 
            !userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        OrderDTO order = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(order);
    }
} 