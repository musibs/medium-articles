package com.musibs.temporal.activities;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Somnath Musib
 * Date: 30/08/2025
 */
public class OrderProcessingWorker {

    private static final Logger logger = LoggerFactory.getLogger(OrderProcessingWorker.class);

    private static final String TASK_QUEUE = "order-processing-queue";


    private final WorkflowServiceStubs serviceStubs;

    private final WorkflowClient workflowClient;

    private final WorkerFactory workerFactory;

    public OrderProcessingWorker() {
        this.serviceStubs = WorkflowServiceStubs.newLocalServiceStubs();
        this.workflowClient = WorkflowClient.newInstance(serviceStubs);
        this.workerFactory = WorkerFactory.newInstance(workflowClient);

        setUpWorker();
    }

    private void setUpWorker() {
        Worker worker = workerFactory.newWorker(TASK_QUEUE);
        worker.registerWorkflowImplementationTypes(OrderWorkflowImpl.class);
        worker.registerActivitiesImplementations(new OrderActivitiesImpl());
        logger.info("Created worker with task queue " + TASK_QUEUE);
    }

    public void start() {
        workerFactory.start();
        logger.info("Order Processing Worker started and listening to task queue: " + TASK_QUEUE);
    }

    public void stop() {
        if(workerFactory != null) {
            workerFactory.shutdown();
        }
        if(serviceStubs != null) {
            serviceStubs.shutdown();
        }
        logger.info("Order Processing Worker stopped.");
    }
}
