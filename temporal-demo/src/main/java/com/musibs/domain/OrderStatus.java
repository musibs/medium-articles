package com.musibs.domain;

/**
 * @author Somnath Musib
 * Date: 30/08/2025
 */
public enum OrderStatus {
    CREATED,
    PAYMENT_PROCESSING,
    PAYMENT_CONFIRMED,
    INVENTORY_RESERVED,
    SHIPPING_ORDER,
    SENDING_NOTIFICATION,
    CANCELLED,
    FAILED,
    COMPLETED
}
