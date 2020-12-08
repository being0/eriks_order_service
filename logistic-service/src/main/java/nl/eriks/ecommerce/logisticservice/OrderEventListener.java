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

        // Just log the message for now
        log.info("Message read from {} is -----> {} ", ORDER_QUEUE, orderEvent);
    }

}
