package com.fiap.gs.infra.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.listener.FatalExceptionStrategy;
import org.springframework.util.ErrorHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class RabbitMQConfig implements RabbitListenerConfigurer {

    // Configurações para cadastro de Guarda Civil
    @Value("${rabbitmq.guarda-civil.queue}")
    private String guardaCivilQueue;

    @Value("${rabbitmq.guarda-civil.exchange}")
    private String guardaCivilExchange;

    @Value("${rabbitmq.guarda-civil.routing-key}")
    private String guardaCivilRoutingKey;

    // Configurações para DLQ (Dead Letter Queue)
    @Value("${rabbitmq.guarda-civil.dlq}")
    private String guardaCivilDLQ;

    @Value("${rabbitmq.guarda-civil.dlx}")
    private String guardaCivilDLX;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter,
            ErrorHandler errorHandler) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        factory.setErrorHandler(errorHandler);

        // Configurações adicionais
        factory.setPrefetchCount(1); // Processar uma mensagem por vez
        factory.setConcurrentConsumers(3); // 3 consumidores simultâneos

        return factory;
    }

    @Bean
    public MessageHandlerMethodFactory messageHandlerMethodFactory(ObjectMapper objectMapper) {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        org.springframework.messaging.converter.MappingJackson2MessageConverter converter =
                new org.springframework.messaging.converter.MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        factory.setMessageConverter(converter);
        return factory;
    }

    @Bean
    public ErrorHandler errorHandler() {
        return new ConditionalRejectingErrorHandler(customExceptionStrategy());
    }

    @Bean
    public FatalExceptionStrategy customExceptionStrategy() {
        return new ConditionalRejectingErrorHandler.DefaultExceptionStrategy() {
            @Override
            public boolean isFatal(Throwable t) {
                log.error("Erro no processamento da mensagem: {}", t.getMessage());
                return super.isFatal(t);
            }
        };
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory(objectMapper()));
    }

    // Configurações específicas para cadastro de Guarda Civil
    @Bean
    public Queue guardaCivilQueue() {
        return QueueBuilder.durable(guardaCivilQueue)
                .withArgument("x-dead-letter-exchange", guardaCivilDLX)
                .withArgument("x-dead-letter-routing-key", guardaCivilDLQ)
                .build();
    }

    @Bean
    public Queue guardaCivilDeadLetterQueue() {
        return QueueBuilder.durable(guardaCivilDLQ).build();
    }

    @Bean
    public TopicExchange guardaCivilExchange() {
        return new TopicExchange(guardaCivilExchange);
    }

    @Bean
    public DirectExchange guardaCivilDeadLetterExchange() {
        return new DirectExchange(guardaCivilDLX);
    }

    @Bean
    public Binding guardaCivilBinding() {
        return BindingBuilder
                .bind(guardaCivilQueue())
                .to(guardaCivilExchange())
                .with(guardaCivilRoutingKey);
    }

    @Bean
    public Binding guardaCivilDeadLetterBinding() {
        return BindingBuilder
                .bind(guardaCivilDeadLetterQueue())
                .to(guardaCivilDeadLetterExchange())
                .with(guardaCivilDLQ);
    }
}