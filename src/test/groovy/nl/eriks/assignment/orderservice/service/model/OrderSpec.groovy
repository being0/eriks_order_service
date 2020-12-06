package nl.eriks.assignment.orderservice.service.model

import spock.lang.Specification

class OrderSpec extends Specification {

    def 'toCreate should prepare order object for creation'() {
        given:
        Order order = new Order(100L, null, 1981121123.89 as BigDecimal, new Date(0), new Date(0), "hacker")
        when:
        order.toCreate("valid_user_id")
        then:
        order.getId() == null
        order.getUserId() == "valid_user_id"
        order.getStatus() == Order.OrderStatus.created
        order.price == new BigDecimal("1981121123.89")
        order.getModified() != new Date(0)
        order.getCreated() != new Date(0)
        order.getCreated() == order.getModified()
    }

}

