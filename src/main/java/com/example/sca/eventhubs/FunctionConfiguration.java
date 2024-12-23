package com.example.sca.eventhubs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration(proxyBeanMethods = false)
public class FunctionConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(FunctionConfiguration.class);

    @Bean
    public Sinks.Many<Message<String>> many() {
        return Sinks.many().unicast().onBackpressureBuffer();
    }

    @Bean
    public Supplier<Flux<Message<String>>> supply(Sinks.Many<Message<String>> many) {
        return () -> many.asFlux()
                         .doOnNext(m -> logger.info("Manually sending message {}", m))
                         .doOnError(t -> logger.error("Error encountered", t));
    }

    @Bean
    public Consumer<Message<String>> consume() {
        return message -> logger.info("New message received: '{}'", message.getPayload());
    }
}
