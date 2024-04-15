package org.wsd.app.config;

import org.axonframework.config.Configurer;
import org.axonframework.eventsourcing.*;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.modelling.command.Repository;
import org.axonframework.spring.config.AxonConfiguration;
import org.axonframework.spring.eventsourcing.SpringAggregateSnapshotterFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.wsd.app.aggregates.ProductAggregate;

import javax.annotation.Nonnull;
import java.util.concurrent.Executors;

@Configuration
public class SnapshotConfig {

    @Bean("productAggregateRepository")
    public Repository<ProductAggregate> productAggregateRepository(EventStore eventStore, SnapshotTriggerDefinition giftCardSnapshotTrigger) {
        return EventSourcingRepository.builder(ProductAggregate.class)
                .snapshotTriggerDefinition(giftCardSnapshotTrigger)
                .eventStore(eventStore)
                .build();
    }

    @Bean
    public SpringAggregateSnapshotterFactoryBean snapshotter() {
        var springAggregateSnapshotterFactoryBean = new SpringAggregateSnapshotterFactoryBean();
        //Setting async executors
        springAggregateSnapshotterFactoryBean.setExecutor(Executors.newSingleThreadExecutor());
        return springAggregateSnapshotterFactoryBean;
    }

    @Bean("productSnapshotTriggerDefinition")
    public SnapshotTriggerDefinition productSnapshotTriggerDefinition(Snapshotter snapshotter) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, 10);
    }
}
