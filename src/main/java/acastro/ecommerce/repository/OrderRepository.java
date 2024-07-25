package acastro.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

import acastro.ecommerce.model.Order;

public interface OrderRepository extends ListCrudRepository<Order, Long> {

    Optional<Order> getOrderById(Long id);
}
