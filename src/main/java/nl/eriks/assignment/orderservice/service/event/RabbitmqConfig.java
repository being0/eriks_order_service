package nl.eriks.assignment.orderservice.service.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

/**
 * Config RabbitmqTemplate to use jackson for serialization.
 * Also create order_topic exchange. There could be later couple of queues registered by different services to consume messages.
 *
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/05/2020
 */
@EnableRabbit
@Configuration
public class RabbitmqConfig {

    public static final String ORDER_TOPIC = "topic.order_event";
    public static final String ORDER_QUEUE = "queue.order_event";

    @Autowired
    private ConnectionFactory rabbitConnectionFactory;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Bean
    public MappingJackson2MessageConverter jackson2Converter() {

        return new MappingJackson2MessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory messageHandlerMethodFactory() {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setMessageConverter(jsonMessageConverter());

        return factory;
    }

    /**
     * Use the same jackson we use in rest services
     *
     * @return message converter
     */
    @Bean
    public MessageConverter jsonMessageConverter() {

        // Use the same jackson mapper we use for our REST API
        ObjectMapper mapper = jacksonObjectMapper.copy();

        // Unfortunately mapper.enableDefaultTyping makes error when we use generics on array, So we can't use something like EntityModificationEvent<AccessRuleTo when entities are array
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator());// This is specially required to serialize/deserialize list of entities, see EntityModificationEvent.entities

        return new Jackson2JsonMessageConverter(mapper);
    }

    /**
     * create rabbit template using jackson for serialization
     *
     * @return rabbitmq template
     */
    @Bean
    public RabbitTemplate createTemplate() {

        RabbitTemplate template = new RabbitTemplate(rabbitConnectionFactory);
        template.setMessageConverter(jsonMessageConverter());

        return template;
    }

    /**
     * Create order_topic exchange, the other services could create queues on this exchange to consume messages.
     *
     * @return order_topic exchange
     */
    @Bean
    TopicExchange orderExchange() {
        return new TopicExchange(ORDER_TOPIC);
    }

    @Bean
    public Queue orderQueue() {

        return new Queue(ORDER_QUEUE);
    }

    @Bean
    public Binding orderQueueBinding() {

        // Define binding to all order events into the queue.order_event
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with("*");
    }

}
