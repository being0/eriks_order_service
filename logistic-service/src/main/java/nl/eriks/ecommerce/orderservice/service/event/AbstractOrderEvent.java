package nl.eriks.ecommerce.orderservice.service.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/05/2020
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public abstract class AbstractOrderEvent implements OrderEvent {

    protected Long orderId;

    protected String userId;

    protected BigDecimal price;

    protected Date orderCreated;

    protected Date raiseDate;

    protected String prevStatus;

}
