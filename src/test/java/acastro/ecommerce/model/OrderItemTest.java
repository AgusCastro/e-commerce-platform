package acastro.ecommerce.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    void getTotal() {
        OrderItem orderItem = new OrderItem();
        orderItem.setUnitPrice(12.2);
        orderItem.setQuantity(2);

        assertEquals(24.4, orderItem.getTotal());
    }
}