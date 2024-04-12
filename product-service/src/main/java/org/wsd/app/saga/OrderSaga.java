package org.wsd.app.saga;

import lombok.extern.log4j.Log4j2;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import org.wsd.app.comamnds.*;
import org.wsd.app.events.*;
import org.wsd.app.payload.UserRestModel;
import org.wsd.app.query.user.FetchUserPaymentDetailsQuery;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Saga
@Log4j2
public class OrderSaga {

    private static final String PAYMENT_PROCESSING_TIMEOUT_DEADLINE = "payment-processing-deadline";
    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient QueryGateway queryGateway;
    @Autowired
    private transient DeadlineManager deadlineManager;

    public OrderSaga() {
    }

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent) {
        final ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
                .orderId(orderCreatedEvent.getOrderId())
                .productId(orderCreatedEvent.getProductId())
                .userId(orderCreatedEvent.getUserId())
                .quantity(orderCreatedEvent.getQuantity())
                .build();
        this.commandGateway.sendAndWait(reserveProductCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent productReservedEvent) {
        log.info("Reserved product with Id: {} and order with Id {}", productReservedEvent.getProductId(), productReservedEvent.getOrderId());

        final FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery = FetchUserPaymentDetailsQuery
                .builder()
                .userId(productReservedEvent.getUserId())
                .build();

        UserRestModel userRestModel = null;
        try {
            userRestModel = queryGateway.query(fetchUserPaymentDetailsQuery, ResponseTypes.instanceOf(UserRestModel.class)).join();
        } catch (Exception ex) {
            log.error("Error while fetching user payment details", ex);
            cancelProductReservation(productReservedEvent);
            return;
        }
        if (userRestModel == null) {
            return;
        }

        log.info("Successfully fetched user payment details" + userRestModel.getPaymentDetails());

        deadlineManager.schedule(Duration.of(10, ChronoUnit.SECONDS), "payment-processing-deadline", productReservedEvent);

        final ProcessPaymentCommand processPaymentCommand = ProcessPaymentCommand.builder()
                .paymentId(UUID.randomUUID().toString())
                .orderId(productReservedEvent.getOrderId())
                .paymentDetails(userRestModel.getPaymentDetails())
                .build();

        String result = null;
        try {
            result = commandGateway.sendAndWait(processPaymentCommand, 10, TimeUnit.SECONDS);
        } catch (Exception ex) {
            log.error("Error while fetching process payment details", ex);
            cancelProductReservation(productReservedEvent);
            return;
        }

        if (result == null) {
            cancelProductReservation(productReservedEvent);

        }

        log.info("Successfully fetched process payment details" + result);
    }

    private void cancelProductReservation(ProductReservedEvent productReservedEvent) {
        final CancelProductReservationCommand cancelProductReservationCommand = CancelProductReservationCommand.builder()
                .productId(productReservedEvent.getProductId())
                .userId(productReservedEvent.getUserId())
                .quantity(productReservedEvent.getQuantity())
                .reason("Could not reserve product")
                .build();
        this.commandGateway.send(cancelProductReservationCommand);
    }


    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservationCancelledEvent productReservationCancelledEvent) {
        final RejectOrderCommand rejectOrderCommand = RejectOrderCommand.builder()
                .orderId(productReservationCancelledEvent.getOrderId())
                .reason(productReservationCancelledEvent.getReason())
                .build();
        this.commandGateway.send(rejectOrderCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderRejectedEvent orderRejectedEvent) {
        log.info("Order rejected with Id: {}", orderRejectedEvent.getOrderId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentProcessedEvent paymentProcessedEvent) {
        deadlineManager.cancelAll("payment-processing-deadline");
        final ApproveOrderCommand approveOrderCommand = ApproveOrderCommand.builder()
                .orderId(paymentProcessedEvent.getOrderId())
                .build();
        this.commandGateway.send(approveOrderCommand);
    }

    @DeadlineHandler(deadlineName = PAYMENT_PROCESSING_TIMEOUT_DEADLINE)
    public void handlePaymentDeadline(ProductReservedEvent productReservedEvent) {
        log.info("Payment processing deadline: {}", productReservedEvent.getProductId());
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderApprovedEvent orderApprovedEvent) {
        log.info("Order is approved. OrderId: {}", orderApprovedEvent.getOrderId());
    }
}
