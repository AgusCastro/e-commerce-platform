package acastro.ecommerce.service.impl;

import java.util.Optional;
import java.util.List;

import org.springframework.stereotype.Service;

import acastro.ecommerce.enums.OrderStatus;
import acastro.ecommerce.exception.InvalidOrderStateException;
import acastro.ecommerce.exception.NotFoundException;
import acastro.ecommerce.model.Order;
import acastro.ecommerce.service.OrderService;
import acastro.ecommerce.repository.OrderRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;


    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.getOrderById(id);
    }

    @Override
    public Order createOrder(String customerName, String deliveryAddress) {
        Order order = new Order(customerName, deliveryAddress);
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Long orderId, Order order) {
        Order existingOrder = orderRepository.getOrderById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        if(existingOrder.getStatus().equals(OrderStatus.CANCELLED) || existingOrder.getStatus().equals(OrderStatus.DELIVERED)) {
            throw new InvalidOrderStateException("Order is already finished");
        }

        existingOrder.setDeliveryDate(order.getDeliveryDate());
        existingOrder.setDeliveryAddress(order.getDeliveryAddress());
        existingOrder.setStatus(order.getStatus());

        return orderRepository.save(existingOrder);
    }

    @Override
    public boolean deleteOrder(Long id) {
        Optional<Order> order = orderRepository.getOrderById(id);

        if(order.isEmpty()) {
            return false;
        }

        order.get().setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order.get());
        return true;
    }
}
