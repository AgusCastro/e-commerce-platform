package acastro.ecommerce.model;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class OrderItemTest {

    @Test
    void getTotal() {
        OrderItem orderItem = new OrderItem();
        orderItem.setUnitPrice(BigDecimal.valueOf(12.2));
        orderItem.setQuantity(2);

        Assertions.assertEquals(BigDecimal.valueOf(24.4), orderItem.getTotal());
    }
}