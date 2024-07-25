package acastro.ecommerce.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import acastro.ecommerce.dto.CreateOrderRequestDto;
import acastro.ecommerce.dto.OrderResponseDto;
import acastro.ecommerce.enums.OrderStatus;
import acastro.ecommerce.exception.NotFoundException;
import acastro.ecommerce.mapper.OrderMapper;
import acastro.ecommerce.model.Order;
import acastro.ecommerce.model.OrderItem;
import acastro.ecommerce.model.Product;
import acastro.ecommerce.service.OrderService;
import acastro.ecommerce.dto.OrderUpdateRequestDto;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Test
    void getAllOrders_EmptyList() {
        Mockito.when(orderService.getAllOrders()).thenReturn(List.of());

        Assertions.assertEquals(0, orderController.getOrders().size());

        Mockito.verify(orderService, Mockito.times(1)).getAllOrders();
    }

    @Test
    void getAllOrders_FilledList() {

        final Product product1 = Mockito.mock(Product.class);
        Mockito.when(product1.getId()).thenReturn(1L);

        final Order order1 = new Order(
                1L,
                LocalDate.now(),
                null,
                "deliveryAddressTest1",
                "test1",
                OrderStatus.PENDING,
                List.of());
        final Order order2 = new Order(
                1L,
                LocalDate.of(2021, 1, 4),
                LocalDate.of(2021, 1, 6),
                "deliveryAddressTest2",
                "test2",
                OrderStatus.DELIVERED,
                new ArrayList<>());
        order2.getItems().add(new OrderItem(123L, product1, order2, 2, 12.2));

        final List<Order> orderList = List.of(order1, order2);

        Mockito.when(orderService.getAllOrders()).thenReturn(orderList);
        final List<OrderResponseDto> expectedResponse = orderList.stream().map(OrderMapper.mapper::toDto).toList();

        final List<OrderResponseDto> orders = orderController.getOrders();

        Assertions.assertEquals(expectedResponse, orders);

        Mockito.verify(orderService, Mockito.times(1)).getAllOrders();
    }

    @Test
    void getOrder() {

        final Long orderId = 1L;

        final Product product = Mockito.mock(Product.class);
        Mockito.when(product.getId()).thenReturn(1L);

        final Order order = new Order(
                orderId,
                LocalDate.of(2021, 1, 4),
                LocalDate.of(2021, 1, 6),
                "deliveryAddressTest2",
                "test2",
                OrderStatus.DELIVERED,
                new ArrayList<>());
        order.getItems().add(new OrderItem(123L, product, order, 2, 12.2));

        Mockito.when(orderService.getOrderById(orderId)).thenReturn(Optional.of(order));
        final OrderResponseDto expectedResponse = OrderMapper.mapper.toDto(order);

        final OrderResponseDto orderResponseDto = orderController.getOrder(orderId);

        Assertions.assertEquals(expectedResponse, orderResponseDto);

        Mockito.verify(orderService, Mockito.times(1)).getOrderById(orderId);
    }

    @Test
    void getOrder_OrderNotFound() {
        final Long orderId = 123L;

        Mockito.when(orderService.getOrderById(orderId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> orderController.getOrder(orderId));
        Mockito.verify(orderService, Mockito.times(1)).getOrderById(orderId);
    }

    @Test
    void createOrder() {
        final CreateOrderRequestDto requestDto = new CreateOrderRequestDto("testCustomer", "testAddress");
        final Order order = new Order(1L, LocalDate.now(), null, "testAddress", "testCustomer", OrderStatus.PENDING, List.of());
        final OrderResponseDto expectedResponse = OrderMapper.mapper.toDto(order);

        Mockito.when(orderService.createOrder(requestDto.customerName(), requestDto.deliveryAddress())).thenReturn(order);

        OrderResponseDto actualResponse = orderController.createOrder(requestDto);

        Mockito.verify(orderService, Mockito.times(1)).createOrder(requestDto.customerName(), requestDto.deliveryAddress());
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void updateOrder() {
        Long orderId = 1L;
        OrderUpdateRequestDto requestDto = new OrderUpdateRequestDto(LocalDate.of(2024, 6, 10), "updatedAddress", OrderStatus.DELIVERED);
        Order mockOrder = new Order(orderId, LocalDate.now(), LocalDate.now(), "updatedAddress", "updatedCustomer", OrderStatus.DELIVERED, List.of());
        OrderResponseDto expectedResponse = OrderMapper.mapper.toDto(mockOrder);

        Mockito.when(orderService.updateOrder(Mockito.eq(orderId), Mockito.any(Order.class))).thenReturn(mockOrder);

        OrderResponseDto actualResponse = orderController.updateOrder(orderId, requestDto);

        Mockito.verify(orderService, Mockito.times(1)).updateOrder(Mockito.eq(orderId), Mockito.any(Order.class));
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void deleteOrder() {
        Long orderId = 1L;

        Mockito.when(orderService.deleteOrder(orderId)).thenReturn(true);

        Boolean actualResponse = orderController.deleteOrder(orderId);

        Mockito.verify(orderService, Mockito.times(1)).deleteOrder(orderId);
        Assertions.assertTrue(actualResponse);

    }
}