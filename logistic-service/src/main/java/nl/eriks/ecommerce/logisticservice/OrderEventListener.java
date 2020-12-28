package nl.eriks.ecommerce.logisticservice;

import lombok.extern.slf4j.Slf4j;
import nl.eriks.ecommerce.orderservice.service.event.OrderEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static nl.eriks.ecommerce.logisticservice.RabbitmqConfig.ORDER_QUEUE;

/**
 * Order event listener
 *
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/08/2020
 */
@Component
@Slf4j
public class OrderEventListener {

    /**
     * Listen on the queue.order_event that is bound to the topic.order_event
     * @param orderEvent order event
     */
    @RabbitListener(queues = ORDER_QUEUE)
    public void listen(OrderEvent orderEvent) {

        // Make sure the listener is Idempotent if the broker config is at-least-once(or any configuration that could send duplicate messages)

        // Just log the message for now
        log.info("A message received from {}. Message -----> {} ", ORDER_QUEUE, orderEvent);
    }

}
