package com.musibs.temporal.activities;

import com.musibs.domain.Order;
import com.musibs.domain.OrderStatus;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;

import java.time.Duration;

/**
 * @author Somnath Musib
 * Date: 30/08/2025
 */
public class OrderWorkflowImpl implements OrderWorkflow{

    private static final Logger logger = Workflow.getLogger(OrderWorkflowImpl.class);

    private OrderStatus currentStatus = OrderStatus.CREATED ;
    private volatile boolean processingComplete = false;
    private volatile boolean cancelRequested = false;

    private String paymentTransactionId;
    private String inventoryReservationId;

    /**
     *  Activity stub with retry and timeout configurations.
     */
    private final OrderActivities activities = Workflow.newActivityStub(OrderActivities.class,
            ActivityOptions.newBuilder()
                    .setScheduleToCloseTimeout(Duration.ofMinutes(5))
                    .setRetryOptions(RetryOptions.newBuilder()
                            .setInitialInterval(Duration.ofSeconds(2))
                            .setMaximumInterval(Duration.ofSeconds(10))
                            .setBackoffCoefficient(2)
                            .setMaximumAttempts(5)
                            .build())
                    .build());

    @Override
    public String processOrder(Order order) {
        logger.info("Starting order processing for order: {}", order.orderId());
        try {
            // Step 1: Process Payment
            currentStatus = OrderStatus.PAYMENT_PROCESSING;
            if(checkCancellation()) {
                handleCancellation();
            }

            paymentTransactionId = activities.processPayment(order);
            currentStatus = OrderStatus.PAYMENT_CONFIRMED;
            logger.info("Payment processed with transaction ID: {}", paymentTransactionId);

            // Step 2: Reserve Inventory
            currentStatus = OrderStatus.INVENTORY_RESERVED;
            if(checkCancellation()) {
                handleCancellation();
            }
            inventoryReservationId = activities.reserveInventory(order);
            logger.info("Inventory reserved with reservation ID: {}", inventoryReservationId);

            // Step 3: Ship Order
            currentStatus = OrderStatus.SHIPPING_ORDER;
            if(checkCancellation()) {
                handleCancellation();
            }
            String trackingNumber = activities.shipOrder(order);
            logger.info("Order shipped with tracking number: {}", trackingNumber);

            // Step 4: Send Notification
            currentStatus = OrderStatus.SENDING_NOTIFICATION;
            activities.sendNotification(order, "Your order has been shipped. Tracking number: " + trackingNumber);
            logger.info("Notification sent to customer for order: {}", order.orderId());

            currentStatus = OrderStatus.COMPLETED;
            logger.info("Order processing completed for order: {}", order.orderId());
            return "Order processed successfully. Tracking number: " + trackingNumber;
        }
        catch (Exception e) {
            logger.error("Error processing order: {}. Initiating compensation. Error: {}", order.orderId(), e.getMessage());
            performCompensation();
            currentStatus = OrderStatus.FAILED;
            throw  e;
        } finally {
            processingComplete = true;
        }
    }

    private boolean checkCancellation() {
        return cancelRequested && !processingComplete;
    }

    /**
     * Handles order cancellation by performing compensation and returning appropriate message.
     */
    private String handleCancellation() {
        logger.info("Processing order cancellation");
        performCompensation();

        currentStatus = OrderStatus.CANCELLED;
        processingComplete = true;

        return "Order was cancelled";
    }

    /**
     * Performs compensating actions to rollback completed steps.
     * This implements the Saga pattern for distributed transaction management.
     */
    private void performCompensation() {
        logger.info("Performing compensation actions");

        // Release inventory if it was reserved
        if (inventoryReservationId != null) {
            try {
                activities.releaseInventory(inventoryReservationId);
                logger.info("Inventory reservation released");
            } catch (Exception e) {
                logger.error("Failed to release inventory reservation: {}", e.getMessage());
                // In a production system, you might want to send this to a dead letter queue
                // or trigger manual intervention
            }
        }

        // Cancel payment if it was processed
        if (paymentTransactionId != null) {
            try {
                activities.cancelPayment(paymentTransactionId);
                logger.info("Payment transaction cancelled");
            } catch (Exception e) {
                logger.error("Failed to cancel payment transaction: {}", e.getMessage());
            }
        }
    }

    @Override
    public void cancelOrder() {
        logger.info("Canceling order");
        this.cancelRequested = true;
    }

    @Override
    public OrderStatus getOrderStatus() {
        return this.currentStatus;
    }

    @Override
    public boolean isProcessingComplete() {
        return this.processingComplete;
    }
}
