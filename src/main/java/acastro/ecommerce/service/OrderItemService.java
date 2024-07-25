package acastro.ecommerce.service;

import java.util.List;
import java.util.Optional;

import acastro.ecommerce.model.OrderItem;

public interface OrderItemService {

    List<OrderItem> getAllOrderItems();
    Optional<OrderItem> getOrderItemById(Long id);
    OrderItem createOrderItem(Long orderId, Long productId, Integer quantity);
    OrderItem updateOrderItem(Long orderItemId, Integer quantity);
    boolean deleteOrderItem(Long id);

}
