package acastro.ecommerce.service;

import java.util.Optional;
import java.util.List;

import acastro.ecommerce.model.Order;

public interface OrderService {

    List<Order> getAllOrders();
    Optional<Order> getOrderById(Long id);
    Order createOrder(String customerName, String deliveryAddress);
    Order updateOrder(Long orderId, Order order);
    boolean deleteOrder(Long id);

}
