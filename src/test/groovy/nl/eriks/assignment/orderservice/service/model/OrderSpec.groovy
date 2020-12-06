package nl.eriks.assignment.orderservice.service.model


import spock.lang.Specification

class OrderSpec extends Specification {

    def 'toCreate should prepare order object for creation'() {

        given:
        Date now = new Date();

        when:
        Order order = Order.toCreate("valid_user_id", 1981121123.89 as BigDecimal);

        then:
        // Check order
        order.getId() == null
        order.getUserId() == "valid_user_id"
        order.getStatus() == Order.OrderStatus.created
        order.price == new BigDecimal("1981121123.89")
        now.time - 1000l < order.getCreated().time
        order.getCreated().time < now.time + 1000l
        order.getCreated() == order.getModified()
    }

    def 'toAccept should change status to accepted and modify the modified date'() {

        given:
        Date now = new Date();
        Order order = Order.toCreate("valid_user_id", 1981121123.89 as BigDecimal);
        order.setId(10L)

        when:
        order.toAccept()

        then:
        // Check order
        order.getStatus() == Order.OrderStatus.accepted
        now.time - 1000l < order.getModified().time
        order.getModified().time < now.time + 1000l
        // Rest of properties remain the same
        order.getId() == 10L
        order.getUserId() == "valid_user_id"
        order.getPrice() == 1981121123.89 as BigDecimal
    }

    def 'toCancel should change status to canceled and modify the modified date'() {

        given:
        Date now = new Date();
        Order order = Order.toCreate("valid_user_id", 1981121123.89 as BigDecimal);
        order.setId(10L)

        when:
        order.toCancel()

        then:
        // Check order
        order.getStatus() == Order.OrderStatus.canceled
        now.time - 1000l < order.getModified().time
        order.getModified().time < now.time + 1000l
        // Rest of properties remain the same
        order.getId() == 10L
        order.getUserId() == "valid_user_id"
        order.getPrice() == 1981121123.89 as BigDecimal
    }

    def 'toDelete should change status to deleted and modify the modified date'() {

        given:
        Date now = new Date();
        Order order = Order.toCreate("valid_user_id", 1981121123.89 as BigDecimal);
        order.setId(10L)

        when:
        order.toDelete()

        then:
        // Check order
        order.getStatus() == Order.OrderStatus.deleted
        now.time - 1000l < order.getModified().time
        order.getModified().time < now.time + 1000l
        // Rest of properties remain the same
        order.getId() == 10L
        order.getUserId() == "valid_user_id"
        order.getPrice() == 1981121123.89 as BigDecimal
    }

    def 'getPrevStatus should show the previous status'() {

        when:
        Order order = Order.toCreate("1", 101.1 as BigDecimal)

        then:
        order.getPrevStatus() == null

        when:
        order.toAccept()

        then:
        order.getPrevStatus() == Order.OrderStatus.created

        when:
        order.toCancel()

        then:
        order.getPrevStatus() == Order.OrderStatus.accepted

        when:
        order.toDelete()

        then:
        order.getPrevStatus() == Order.OrderStatus.canceled

    }

}

