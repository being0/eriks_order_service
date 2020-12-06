package nl.eriks.assignment.orderservice.service.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Order publishing should be reliable and quick, it is suggested to use OUTBOX table and do the order transaction and
 * event generation in the same ACID transaction, and then another service/thread should read from OUTBOX table and send
 * the events. The other option is to use Event Sourcing pattern using a reliable provider.
 * <p>
 * However here for simplicity I just send to RabbitMq in a non atomic transaction.
 *
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/05/2020
 */
@Component
@Slf4j
public class DefaultOrderPublisher implements OrderEventPublisher {

    @Override
    public void publish(OrderEvent orderEvent) {

        log.debug("Publishing {}, values are {}", orderEvent.getClass(), orderEvent);
    }
}
