package org.wsd.app.saga;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import org.wsd.app.comamnds.ApproveOrderCommand;
import org.wsd.app.comamnds.ProcessPaymentCommand;
import org.wsd.app.comamnds.ReserveProductCommand;
import org.wsd.app.events.OrderApprovedEvent;
import org.wsd.app.events.OrderCreatedEvent;
import org.wsd.app.events.PaymentProcessedEvent;
import org.wsd.app.events.ProductReservedEvent;
import org.wsd.app.payload.UserRestModel;
import org.wsd.app.query.user.FetchUserPaymentDetailsQuery;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Saga
@Log4j2
public class OrderSaga {

    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient QueryGateway queryGateway;

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
            return;
        }
        if (userRestModel == null) {
            return;
        }

        log.info("Successfully fetched user payment details" + userRestModel.getPaymentDetails());

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
        }

        if (result != null) {
            log.info("Successfully fetched process payment details" + result);
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentProcessedEvent paymentProcessedEvent) {
        final ApproveOrderCommand approveOrderCommand = ApproveOrderCommand.builder()
                .orderId(paymentProcessedEvent.getOrderId())
                .build();
        this.commandGateway.send(approveOrderCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderApprovedEvent orderApprovedEvent) {
        log.info("Order is approved. OrderId: {}", orderApprovedEvent.getOrderId());
    }
}
