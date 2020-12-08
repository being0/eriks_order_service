package nl.eriks.ecommerce.orderservice.service.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/05/2020
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class OrderAcceptedEvent extends AbstractOrderEvent {

}
