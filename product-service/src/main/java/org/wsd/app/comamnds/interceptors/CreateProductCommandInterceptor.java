package org.wsd.app.comamnds.interceptors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.axonframework.modelling.command.CommandHandlerInterceptor;
import org.springframework.stereotype.Component;
import org.wsd.app.comamnds.CreateProductCommand;
import org.wsd.app.domain.ProductLookupEntity;
import org.wsd.app.repository.ProductLookRepository;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
    private final ProductLookRepository productLookRepository;

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {
            // You may need to adjust the following check if your interceptor logic is specific to CreateProductCommand
            if (command.getPayloadType().equals(CreateProductCommand.class)) {
                log.info("Intercepted Command : " + command.getPayloadType());
                CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();

                Optional<ProductLookupEntity> lookupEntity = productLookRepository.findById(createProductCommand.getProductId());
                if (lookupEntity.isPresent()) {
                    log.info("Product Id : " + createProductCommand.getProductId() + " already exists");
                    throw new IllegalArgumentException("Product Id " + createProductCommand.getProductId() + " already exists");
                }

                if (createProductCommand.getProductName() == null || createProductCommand.getProductName().isEmpty()) {
                    throw new IllegalArgumentException("Product name cannot be empty");
                }
                if (createProductCommand.getPrice() <= 0) {
                    throw new IllegalArgumentException("Price must be greater than zero");
                }


            }
            return command;
        };
    }
}