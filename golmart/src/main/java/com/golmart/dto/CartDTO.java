package com.golmart.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CartDTO {
    private Long id;
    private List<CartItemDTO> items;
    private BigDecimal totalAmount;
    private int totalItems;

    @Data
    public static class CartItemDTO {
        private Long id;
        private ProductDTO product;
        private int quantity;
        private BigDecimal subtotal;
    }

    @Data
    public static class ProductDTO {
        private Long id;
        private String name;
        private String imageUrl;
        private BigDecimal price;
        private Integer stock;
    }
} 