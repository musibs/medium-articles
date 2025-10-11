package com.musibs.temporal.activities;


import com.musibs.domain.Order;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

/**
 * @author Somnath Musib
 * Date: 30/08/2025
 */
@ActivityInterface
public interface OrderActivities {

    @ActivityMethod
    String processPayment(Order order);

    @ActivityMethod
    void cancelPayment(String transactionId);

    @ActivityMethod
    String shipOrder(Order order);

    @ActivityMethod
    String reserveInventory(Order order);

    @ActivityMethod
    void releaseInventory(String reservationId);

    @ActivityMethod
    void sendNotification(Order order, String message);
}
