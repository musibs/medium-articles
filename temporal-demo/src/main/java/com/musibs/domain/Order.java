package com.musibs.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Somnath Musib
 * Date: 30/08/2025
 */
public record Order(String orderId, String customerId, BigDecimal amount,
                    String productId, LocalDateTime createdAt, OrderStatus status) {

    public Order {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order ID cannot be null or blank");
        }
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("Customer ID cannot be null or blank");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("Product ID cannot be null or blank");
        }
    }

    public static Order createNew(String customerId, BigDecimal amount, String productId) {
        return new Order(
            UUID.randomUUID().toString(),
            customerId,
            amount,
            productId,
            LocalDateTime.now(),
            OrderStatus.CREATED
        );
    }
}
