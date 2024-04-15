package org.wsd.app.config;

import org.axonframework.config.ConfigurationScopeAwareProvider;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.SimpleDeadlineManager;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeadlineManagerConfig {

    @Bean
    public DeadlineManager deadlineManager(org.axonframework.config.Configuration configuration,
                                           SpringTransactionManager springTransactionManager) {
        return SimpleDeadlineManager.builder()
            .scopeAwareProvider(new ConfigurationScopeAwareProvider(configuration))
            .transactionManager(springTransactionManager)
            .build();
    }
}