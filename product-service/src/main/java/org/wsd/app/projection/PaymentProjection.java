package org.wsd.app.projection;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.wsd.app.domain.PaymentEntity;
import org.wsd.app.events.PaymentProcessedEvent;
import org.wsd.app.repository.PaymentRepository;


@Log4j2
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@ProcessingGroup("payment-group")
public class PaymentProjection {

    private final PaymentRepository paymentRepository;

    @EventHandler
    public void handle(PaymentProcessedEvent paymentProcessedEvent) {
        final PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setPaymentId(paymentEntity.getPaymentId());
        paymentEntity.setOrderId(paymentProcessedEvent.getOrderId());
        this.paymentRepository.save(paymentEntity);
    }

}
