package com.musibs.temporal.activities;

import com.musibs.domain.Order;
import com.musibs.domain.OrderStatus;
import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

/**
 * @author Somnath Musib
 * Date: 30/08/2025
 */
@WorkflowInterface
public interface OrderWorkflow {

    /**
     * Main workflow method that processes an order from creation to completion.
     * This method defines the business logic and coordinates various activities.
     *
     * @param order The order to process
     * @return Final order status message
     */
    @WorkflowMethod
    String processOrder(Order order);

    /**
     * Signal method to cancel the order processing.
     * This can be invoked externally to request cancellation of the workflow.
     */
    @SignalMethod
    void cancelOrder();

    /**
     * Query method to get the current status of the order processing.
     * This can be invoked externally to check the progress of the workflow.
     *
     * @return Current order status
     */
    @QueryMethod
    OrderStatus getOrderStatus();

    /**
     * Query method to check if the order processing is complete.
     *
     * @return true if processing is complete, false otherwise
     */
    @QueryMethod
    boolean isProcessingComplete();
}
