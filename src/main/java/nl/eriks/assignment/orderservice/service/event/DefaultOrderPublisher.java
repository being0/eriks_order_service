package nl.eriks.assignment.orderservice.service.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Order publishing should be reliable and quick, it is suggested to use OUTBOX table and do the order transaction and
 * event generation in the same ACID transaction, and then another service/thread should read from OUTBOX table and send
 * the events. The other option is to use Event Sourcing pattern using a reliable provider.(Two phase commit is slow and too complex)
 * <p>
 * However here for simplicity I just send to RabbitMq in a non atomic transaction
 *
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/05/2020
 */
@Component
@Slf4j
public class DefaultOrderPublisher implements OrderEventPublisher {

    @Resource
    protected RabbitTemplate rabbitTemplate;

    @Override
    public void publish(OrderEvent orderEvent) {

        log.debug("Publishing {}, values are {}", orderEvent.getClass(), orderEvent);

        // I just simply send the event to rabbitmq, but mature solution should have reties in case of error
        // We probably need an OUTBOX message and then another thread to do the below action
        // Because of complexity of this approach we may need to consider event sourcing as an option
        rabbitTemplate.convertAndSend(RabbitmqConfig.ORDER_TOPIC, createRoutingKey(orderEvent), orderEvent);

    }

    private String createRoutingKey(OrderEvent orderEvent) {

        // Use the event class simple name for routing
        return orderEvent.getClass().getSimpleName();
    }

    @RabbitListener(queues = RabbitmqConfig.ORDER_QUEUE)
    public void listen(OrderEvent orderEvent) {
        log.info("Message read from myQueue : " + orderEvent);
    }
}
