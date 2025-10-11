package com.musibs.temporal.activities;


import com.musibs.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * @author Somnath Musib
 * Date: 30/08/2025
 */
public class OrderActivitiesImpl implements OrderActivities {

    private static final Logger logger = LoggerFactory.getLogger(OrderActivitiesImpl.class);
    private final SecureRandom random = new SecureRandom();

    @Override
    public String processPayment(Order order) {
        logger.info("Processing payment for order: {}", order.orderId());
        // Simulate processing time
        simulateDelay(1000, 3000);

        // Simulate occasional failures (10% chance)
        if (random.nextInt(10) == 0) {
            throw new RuntimeException("Payment processing failed for order: " + order.orderId());
        }

        String transactionId = "TXN-" + UUID.randomUUID().toString();
        logger.info("Payment processed successfully. Transaction ID: {}", transactionId);
        return transactionId;
    }

    @Override
    public void cancelPayment(String transactionId) {
        logger.info("Canceling payment transaction: {}", transactionId);

        simulateDelay(500, 1500);

        logger.info("Payment transaction canceled: {}", transactionId);
    }

    @Override
    public String shipOrder(Order order) {
        logger.info("Shipping order: {}", order.orderId());

        simulateDelay(2000, 4000);

        // Simulate occasional failures (3% chance)
        if (random.nextInt(33) == 0) {
            throw new RuntimeException("Shipping service unavailable for order: " + order.orderId());
        }

        String trackingNumber = "TRACK-" + UUID.randomUUID().toString();
        logger.info("Order shipped successfully. Tracking number: {}", trackingNumber);
        return trackingNumber;
    }

    @Override
    public String reserveInventory(Order order) {
        logger.info("Reserving inventory for order: {}", order.orderId());
        simulateDelay(500, 2000);

        // Simulate occasional failures (5% chance)
        if (random.nextInt(20) == 0) {
            throw new RuntimeException("Insufficient inventory for product: " + order.productId());
        }
        String reservationId = "RES-" + UUID.randomUUID().toString();
        logger.info("Inventory reserved successfully. Reservation ID: {}", reservationId);
        return reservationId;
    }

    @Override
    public void releaseInventory(String reservationId) {
        logger.info("Releasing inventory reservation: {}", reservationId);

        simulateDelay(300, 1000);

        logger.info("Inventory reservation released: {}", reservationId);
    }

    @Override
    public void sendNotification(Order order, String message) {
        logger.info("Sending notification for order {}: {}", order.orderId(), message);

        simulateDelay(200, 800);

        // Simulate occasional failures (2% chance)
        if (random.nextInt(50) == 0) {
            throw new RuntimeException("Notification service unavailable");
        }

        logger.info("Notification sent successfully to customer: {}", order.customerId());
    }

    private void simulateDelay(int minMs, int maxMs) {
        try {
            int delay = random.nextInt(maxMs - minMs) + minMs;
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Activity interrupted", e);
        }
    }
}
