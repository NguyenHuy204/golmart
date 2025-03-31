package com.golmart.dto;

import com.golmart.entity.OrderStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long accountId;
    private List<OrderItemDTO> items;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private String shippingAddress;
    private String phone;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    public static class OrderItemDTO {
        private Long id;
        private ProductDTO product;
        private Integer quantity;
        private BigDecimal price;
        private BigDecimal subtotal;
    }

    @Data
    public static class CheckoutRequest {
        private String shippingAddress;
        private String phone;
        private String note;
    }
} 