package nl.eriks.ecommerce.orderservice.service.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.stream.Stream;

/**
 * Order model
 *
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/04/2020
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders")
public class Order implements Domain {

    /**
     * Order Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Order status
     */
    @Column(name = "status")
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Integer statusId;

    /**
     * Archive previous status when an event occurs, it is used for event data
     */
    @Transient
    @Setter(AccessLevel.NONE)
    private OrderStatus prevStatus;

    /**
     * Order price
     */
    @Column(name = "price")
    private BigDecimal price;

    /**
     * Date of order creation
     */
    @Column(name = "created")
    private Date created;

    /**
     * Date order modified
     */
    @Column(name = "modified")
    private Date modified;

    /**
     * Order owner user id
     */
    @Column(name = "user_id")
    private String userId;

    public Order(Long id, OrderStatus status, BigDecimal price, Date created, Date modified, String userId) {
        this.id = id;
        setStatus(status);
        this.price = price;
        this.created = created;
        this.modified = modified;
        this.userId = userId;
    }

    public void setStatus(OrderStatus status) {
        // Archive previous status in prevStatus
        this.prevStatus = getStatus();

        this.statusId = status == null ? null : status.getValue();
    }

    public OrderStatus getStatus() {
        if (statusId == null) return null;

        return OrderStatus.of(statusId);
    }

    public static Order toCreate(String userId, BigDecimal price) {

        Date currentTime = getCurrentTime();

        if (price == null || BigDecimal.ZERO.compareTo(price) > 0) {
            throw new ValidationException("Price can not be null or under zero!");
        }

        return new Order(null, OrderStatus.created, price, currentTime, currentTime, userId);
    }

    public Order toAccept() {

        setModified(getCurrentTime());
        setStatus(Order.OrderStatus.accepted);

        return this;
    }

    public Order toCancel() {

        setModified(getCurrentTime());
        setStatus(OrderStatus.canceled);

        return this;
    }

    public Order toDelete() {

        setModified(getCurrentTime());
        setStatus(OrderStatus.deleted);

        return this;
    }

    public enum OrderStatus {

        created(1), accepted(2), canceled(3), deleted(4);

        private Integer value;

        OrderStatus(Integer value) {
            this.value = value;
        }

        int getValue() {
            return value;
        }

        public static OrderStatus of(Integer value) {
            if (value == null) return null;

            return Stream.of(OrderStatus.values())
                    .filter(p -> p.getValue() == value)
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
    }


    public static Date getCurrentTime() {

        return new Date();
    }
}
