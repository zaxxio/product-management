package org.wsd.app.config;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.wsd.app.comamnds.interceptors.CreateProductCommandInterceptor;
import org.wsd.app.listener.ProductServiceEventsErrorHandler;

@Configuration
public class CommandBusConfig {


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
