package com.golmart.repository;

import com.golmart.entity.Order;
import com.golmart.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByAccountIdOrderByCreatedAtDesc(Long accountId);
    List<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status);
    List<Order> findByAccountIdAndStatusOrderByCreatedAtDesc(Long accountId, OrderStatus status);
} 