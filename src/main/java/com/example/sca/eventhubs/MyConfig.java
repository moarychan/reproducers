package com.example.sca.eventhubs;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class MyConfig {

    @Bean
    @ConditionalOnProperty("a.b.connection-string")
    String testBean() {
        return "test";
    }
}
