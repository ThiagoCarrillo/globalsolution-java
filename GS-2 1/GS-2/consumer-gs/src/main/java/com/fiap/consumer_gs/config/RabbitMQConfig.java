package com.fiap.consumer_gs.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Value("${rabbitmq.guarda-civil.queue}")
    private String guardaCivilQueue;

    @Value("${rabbitmq.guarda-civil.exchange}")
    private String guardaCivilExchange;

    @Value("${rabbitmq.guarda-civil.routing-key}")
    private String guardaCivilRoutingKey;

    @Value("${rabbitmq.guarda-civil.dlq}")
    private String guardaCivilDLQ;

    @Value("${rabbitmq.guarda-civil.dlx}")
    private String guardaCivilDLX;

    // 1. Conversor de mensagens JSON
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);

        // Configuração para rejeitar mensagens com erro (envia para DLQ)
        factory.setDefaultRequeueRejected(false);

        return factory;
    }

    // 3. Configuração do RabbitTemplate
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }

    // Configurações existentes abaixo...
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