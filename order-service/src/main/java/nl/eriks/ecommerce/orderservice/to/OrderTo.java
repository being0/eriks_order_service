package nl.eriks.ecommerce.orderservice.to;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Order transfer object
 *
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/04/2020
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderTo {

    /**
     * Order Id
     */
    private Long id;

    /**
     * Order status
     */
    private OrderStatusTo status;

    /**
     * Order price
     */
    @NotNull
    private BigDecimal price;

    /**
     * Date of order creation
     */
    private Date created;

    /**
     * Date order modified
     */
    private Date modified;

    /**
     * Order owner user id
     */
    private String userId;

    public enum OrderStatusTo {
        created, accepted, canceled, deleted
    }
}
