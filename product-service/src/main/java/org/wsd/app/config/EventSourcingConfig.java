package org.wsd.app.config;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.Configuration;
import org.axonframework.config.ConfigurationScopeAwareProvider;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.SimpleDeadlineManager;
import org.axonframework.eventsourcing.AggregateSnapshotter;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.wsd.app.comamnds.interceptors.CreateProductCommandInterceptor;
import org.wsd.app.listener.ProductServiceEventsErrorHandler;

@org.springframework.context.annotation.Configuration
public class EventSourcingConfig {


    @Autowired
    public void configure(CommandBus commandBus, CreateProductCommandInterceptor interceptor) {
        commandBus.registerDispatchInterceptor(interceptor);
    }

    @Autowired
    public void configure(EventProcessingConfigurer eventProcessingConfigurer) {
//        eventProcessingConfigurer.registerListenerInvocationErrorHandler(
//                "product-group",
//                configuration -> PropagatingErrorHandler.instance()
//        );
        eventProcessingConfigurer.registerListenerInvocationErrorHandler(
                "product-group",
                configuration -> new ProductServiceEventsErrorHandler()
        );
    }

}
