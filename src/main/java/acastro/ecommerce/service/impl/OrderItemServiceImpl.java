package acastro.ecommerce.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import acastro.ecommerce.enums.OrderStatus;
import acastro.ecommerce.exception.InvalidOrderStateException;
import acastro.ecommerce.exception.ItemAlreadyExistsException;
import acastro.ecommerce.exception.NotFoundException;
import acastro.ecommerce.exception.OutOfStockException;
import acastro.ecommerce.model.Order;
import acastro.ecommerce.model.OrderItem;
import acastro.ecommerce.model.Product;
import acastro.ecommerce.repository.ProductRepository;
import acastro.ecommerce.service.OrderItemService;
import acastro.ecommerce.repository.OrderItemRepository;
import acastro.ecommerce.repository.OrderRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    @Override
    public Optional<OrderItem> getOrderItemById(Long id) {
        return orderItemRepository.getOrderItemById(id);
    }

    @Override
    public OrderItem createOrderItem(Long orderId, Long productId, Integer quantity) {
        Optional<OrderItem> existingOrderItem = orderItemRepository.getOrderItemByOrderIdAndProductId(orderId, productId);

        if(existingOrderItem.isPresent()) {
            throw new ItemAlreadyExistsException(productId);
        }

        Order order = checkOrder(orderId);
        Product product = checkProduct(productId, quantity);

        OrderItem orderItem = new OrderItem(null, product, order, quantity, product.getPrice());

        return orderItemRepository.save(orderItem);
    }

    @Override
    public OrderItem updateOrderItem(Long orderItemId, Integer quantity) {
        OrderItem existingOrderItem = orderItemRepository.getOrderItemById(orderItemId)
                .orElseThrow(() -> new NotFoundException("Order Item not found"));

        checkOrder(existingOrderItem.getOrder().getId());
        checkProduct(existingOrderItem.getProduct().getId(), quantity);

        existingOrderItem.setQuantity(quantity);

        return orderItemRepository.save(existingOrderItem);
    }

    @Override
    public boolean deleteOrderItem(Long id) {
        Optional<OrderItem> orderItem = orderItemRepository.getOrderItemById(id);
        if(orderItem.isEmpty()) {
            return false;
        }
        checkOrder(orderItem.get().getOrder().getId());
        orderItemRepository.delete(orderItem.get());
        return true;
    }

    private Order checkOrder(Long orderId) {
        // Check existing order and status
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        if(order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStateException("Order is not pending");
        }
        return order;
    }

    private Product checkProduct(Long productId, Integer quantity) {
        // Check product availability
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        if(product.getStock() < quantity) {
            throw new OutOfStockException(productId);
        }
        return product;
    }
}
