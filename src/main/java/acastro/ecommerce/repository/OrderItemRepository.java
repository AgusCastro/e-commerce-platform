package acastro.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

import acastro.ecommerce.model.OrderItem;

public interface OrderItemRepository extends ListCrudRepository<OrderItem, Long> {

    Optional<OrderItem> getOrderItemById(Long id);

    Optional<OrderItem> getOrderItemByOrderIdAndProductId(Long orderId, Long productId);
}
