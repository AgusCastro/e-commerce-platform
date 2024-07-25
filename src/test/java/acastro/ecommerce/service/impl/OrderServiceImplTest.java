package acastro.ecommerce.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import acastro.ecommerce.enums.OrderStatus;
import acastro.ecommerce.exception.NotFoundException;
import acastro.ecommerce.model.Order;
import acastro.ecommerce.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;


    @Test
    void getAllOrders_EmptyResult() {
        Mockito.when(orderRepository.findAll()).thenReturn(List.of());
        List<Order> orders = orderService.getAllOrders();

        Assertions.assertEquals(0, orders.size());

        Mockito.verify(orderRepository, Mockito.times(1)).findAll();
    }

    @Test
    void getAllOrders_WithResult() {
        Mockito.when(orderRepository.findAll()).thenReturn(List.of(Mockito.mock(Order.class), Mockito.mock(Order.class)));

        List<Order> orders = orderService.getAllOrders();

        Assertions.assertEquals(2, orders.size());
        Mockito.verify(orderRepository, Mockito.times(1)).findAll();
    }

    @Test
    void getOrderById() {
        Order order = Mockito.mock(Order.class);
        Mockito.when(orderRepository.getOrderById(1L)).thenReturn(Optional.of(order));

        Order foundOrder = orderService.getOrderById(1L).orElseThrow();

        Assertions.assertEquals(order, foundOrder);
        Mockito.verify(orderRepository).getOrderById(1L);
    }

    @Test
    void getOrderById_NotFound() {
        Mockito.when(orderRepository.getOrderById(1L)).thenReturn(Optional.empty());

        Optional<Order> result = orderService.getOrderById(1L);

        Assertions.assertTrue(result.isEmpty());

        Mockito.verify(orderRepository).getOrderById(1L);
    }

    @Test
    void createOrder() {
        final String customerName = "customerName";
        final String deliveryAddress = "deliveryAddress";
        final Long createdId = 1L;

        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenAnswer(invocation -> {
            Order save = invocation.getArgument(0);
            save.setId(createdId);
            return invocation.getArgument(0);
        });

        Order savedOrder = orderService.createOrder(customerName, deliveryAddress);

        Assertions.assertEquals(createdId, savedOrder.getId());
        Assertions.assertEquals(customerName, savedOrder.getCustomerName());
        Assertions.assertEquals(deliveryAddress, savedOrder.getDeliveryAddress());

        Mockito.verify(orderRepository, Mockito.times(1)).save(Mockito.any(Order.class));
    }

    @Test
    void updateOrder() {
        Order existingOrder = new Order(1L, LocalDate.of(2023, 4, 1), null, "Delivery address", "test", OrderStatus.PENDING, List.of());

        Order updatedOrder = new Order(1L, LocalDate.of(2023, 4, 1), LocalDate.of(2023, 4, 20), "latest address", "test", OrderStatus.SHIPPED, List.of());

        Mockito.when(orderRepository.getOrderById(1L)).thenReturn(Optional.of(existingOrder));
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.updateOrder(1L, updatedOrder);

        Assertions.assertEquals(updatedOrder.getDeliveryDate(), result.getDeliveryDate());
        Assertions.assertEquals(updatedOrder.getDeliveryAddress(), result.getDeliveryAddress());
        Assertions.assertEquals(updatedOrder.getStatus(), result.getStatus());

        Mockito.verify(orderRepository, Mockito.times(1)).getOrderById(1L);
        Mockito.verify(orderRepository, Mockito.times(1)).save(Mockito.any(Order.class));
    }

    @Test
    void updateOrder_NotFound() {
        Order updatedOrder = Mockito.mock(Order.class);
        Mockito.when(orderRepository.getOrderById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> orderService.updateOrder(1L, updatedOrder));

        Mockito.verify(orderRepository, Mockito.times(1)).getOrderById(1L);
        Mockito.verify(orderRepository, Mockito.never()).save(Mockito.any(Order.class));
    }

    @Test
    void deleteOrder() {
        Order order = new Order("customerName", "deliveryAddress");
        Mockito.when(orderRepository.getOrderById(1L)).thenReturn(Optional.of(order));

        boolean result = orderService.deleteOrder(1L);

        Assertions.assertTrue(result);

        Mockito.verify(orderRepository, Mockito.times(1)).getOrderById(1L);
        Mockito.verify(orderRepository, Mockito.times(1)).save(Mockito.any(Order.class));
    }

    @Test
    void deleteOrder_NotFound() {
        Mockito.when(orderRepository.getOrderById(1L)).thenReturn(Optional.empty());

        boolean result = orderService.deleteOrder(1L);

        Assertions.assertFalse(result);

        Mockito.verify(orderRepository).getOrderById(1L);
        Mockito.verify(orderRepository, Mockito.never()).save(Mockito.any(Order.class));
    }
}