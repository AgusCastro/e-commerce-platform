package acastro.ecommerce.service.impl;

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
import acastro.ecommerce.exception.InvalidOrderStateException;
import acastro.ecommerce.exception.ItemAlreadyExistsException;
import acastro.ecommerce.exception.NotFoundException;
import acastro.ecommerce.exception.OutOfStockException;
import acastro.ecommerce.model.Order;
import acastro.ecommerce.model.OrderItem;
import acastro.ecommerce.model.Product;
import acastro.ecommerce.repository.OrderItemRepository;
import acastro.ecommerce.repository.OrderRepository;
import acastro.ecommerce.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceImplTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    @Test
    void getAllOrderItems() {
        Product product = Mockito.mock(Product.class);
        Order order = Mockito.mock(Order.class);

        OrderItem orderItem = new OrderItem(1L, product, order, 5, 4.0);

        List<OrderItem> expectedResponse = List.of(orderItem);

        Mockito.when(orderItemRepository.findAll()).thenReturn(List.of(orderItem));

        List<OrderItem> response = orderItemService.getAllOrderItems();

        Assertions.assertEquals(expectedResponse, response);

        Mockito.verify(orderItemRepository, Mockito.times(1)).findAll();
    }

    @Test
    void getOrderItemById() {
        Product product = Mockito.mock(Product.class);
        Order order = Mockito.mock(Order.class);

        OrderItem orderItem = new OrderItem(1L, product, order, 5, 4.0);

        Mockito.when(orderItemRepository.getOrderItemById(1L)).thenReturn(Optional.of(orderItem));

        Optional<OrderItem> response = orderItemService.getOrderItemById(1L);

        Assertions.assertTrue(response.isPresent());
        Assertions.assertEquals(orderItem, response.get());

        Mockito.verify(orderItemRepository, Mockito.times(1)).getOrderItemById(1L);
    }

    @Test
    void getOrderItemById_NotFound() {
        Mockito.when(orderItemRepository.getOrderItemById(1L)).thenReturn(Optional.empty());

        Optional<OrderItem> response = orderItemService.getOrderItemById(1L);

        Assertions.assertTrue(response.isEmpty());

        Mockito.verify(orderItemRepository, Mockito.times(1)).getOrderItemById(1L);
    }

    @Test
    void createOrderItem_OrderNotFound() {
        Mockito.when(orderItemRepository.getOrderItemByOrderIdAndProductId(22L, 11L))
                .thenReturn(Optional.empty());
        Mockito.when(orderRepository.findById(22L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> orderItemService.createOrderItem(22L, 11L, 5));

        Mockito.verify(orderItemRepository, Mockito.times(1)).getOrderItemByOrderIdAndProductId(22L, 11L);
        Mockito.verify(orderRepository, Mockito.times(1)).findById(22L);
        Mockito.verify(productRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(orderItemRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void createOrderItem_ExistingOrderItemWithSameProduct() {
        Mockito.when(orderItemRepository.getOrderItemByOrderIdAndProductId(22L, 11L))
                .thenReturn(Optional.of(Mockito.mock(OrderItem.class)));

        Assertions.assertThrows(ItemAlreadyExistsException.class, () -> orderItemService.createOrderItem(22L, 11L, 5));

        Mockito.verify(orderItemRepository, Mockito.times(1)).getOrderItemByOrderIdAndProductId(22L, 11L);
        Mockito.verify(orderRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(productRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(orderItemRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void createOrderItem_OrderNotPending() {
        Mockito.when(orderItemRepository.getOrderItemByOrderIdAndProductId(22L, 11L))
                .thenReturn(Optional.empty());
        Order order = Mockito.mock(Order.class);
        Mockito.when(order.getStatus()).thenReturn(OrderStatus.DELIVERED);

        Mockito.when(orderRepository.findById(22L)).thenReturn(Optional.of(order));

        Assertions.assertThrows(InvalidOrderStateException.class, () -> orderItemService.createOrderItem(22L, 11L, 5));

        Mockito.verify(orderItemRepository, Mockito.times(1)).getOrderItemByOrderIdAndProductId(22L, 11L);
        Mockito.verify(orderRepository, Mockito.times(1)).findById(22L);
        Mockito.verify(productRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(orderItemRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void createOrderItem_ProductNotFound() {
        Mockito.when(orderItemRepository.getOrderItemByOrderIdAndProductId(22L, 11L))
                .thenReturn(Optional.empty());
        Order order = Mockito.mock(Order.class);
        Mockito.when(order.getStatus()).thenReturn(OrderStatus.PENDING);

        Mockito.when(orderRepository.findById(22L)).thenReturn(Optional.of(order));
        Mockito.when(productRepository.findById(11L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> orderItemService.createOrderItem(22L, 11L, 5));

        Mockito.verify(orderItemRepository, Mockito.times(1)).getOrderItemByOrderIdAndProductId(22L, 11L);
        Mockito.verify(orderRepository, Mockito.times(1)).findById(22L);
        Mockito.verify(productRepository, Mockito.times(1)).findById(11L);
        Mockito.verify(orderItemRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void createOrderItem_ProductOutOfStock() {
        Mockito.when(orderItemRepository.getOrderItemByOrderIdAndProductId(22L, 11L))
                .thenReturn(Optional.empty());
        Product product = Mockito.mock(Product.class);
        Mockito.when(product.getStock()).thenReturn(4);

        Order order = Mockito.mock(Order.class);
        Mockito.when(order.getStatus()).thenReturn(OrderStatus.PENDING);

        Mockito.when(orderRepository.findById(22L)).thenReturn(Optional.of(order));
        Mockito.when(productRepository.findById(11L)).thenReturn(Optional.of(product));

        Assertions.assertThrows(OutOfStockException.class, () -> orderItemService.createOrderItem(22L, 11L, 5));

        Mockito.verify(orderItemRepository, Mockito.times(1)).getOrderItemByOrderIdAndProductId(22L, 11L);
        Mockito.verify(orderRepository, Mockito.times(1)).findById(22L);
        Mockito.verify(productRepository, Mockito.times(1)).findById(11L);
        Mockito.verify(orderItemRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void createOrderItem() {
        Mockito.when(orderItemRepository.getOrderItemByOrderIdAndProductId(22L, 11L))
                .thenReturn(Optional.empty());
        Product product = Mockito.mock(Product.class);
        Mockito.when(product.getStock()).thenReturn(10);
        Mockito.when(product.getPrice()).thenReturn(4.0);

        Order order = Mockito.mock(Order.class);
        Mockito.when(order.getStatus()).thenReturn(OrderStatus.PENDING);

        Mockito.when(orderRepository.findById(22L)).thenReturn(Optional.of(order));
        Mockito.when(productRepository.findById(11L)).thenReturn(Optional.of(product));

        OrderItem expectedOrderItem = new OrderItem(null, product, order, 5, 4.0);

        Mockito.when(orderItemRepository.save(Mockito.any())).thenReturn(expectedOrderItem);

        OrderItem response = orderItemService.createOrderItem(22L, 11L, 5);

        Assertions.assertEquals(expectedOrderItem, response);

        Mockito.verify(orderItemRepository, Mockito.times(1)).getOrderItemByOrderIdAndProductId(22L, 11L);
        Mockito.verify(orderRepository, Mockito.times(1)).findById(22L);
        Mockito.verify(productRepository, Mockito.times(1)).findById(11L);
        Mockito.verify(orderItemRepository, Mockito.times(1)).save(Mockito.any());

    }

    @Test
    void updateOrderItem_orderItemNotFound() {
        Mockito.when(orderItemRepository.getOrderItemById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> orderItemService.updateOrderItem(1L, 5));

        Mockito.verify(orderItemRepository, Mockito.times(1)).getOrderItemById(1L);
        Mockito.verify(orderRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(productRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(orderItemRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void updateOrderItem_orderNotFound() {
        Product product = Mockito.mock(Product.class);
        Order order = Mockito.mock(Order.class);

        Mockito.when(order.getId()).thenReturn(22L);

        OrderItem orderItem = new OrderItem(1L, product, order, 5, 4.0);

        Mockito.when(orderItemRepository.getOrderItemById(1L)).thenReturn(Optional.of(orderItem));
        Mockito.when(orderRepository.findById(22L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> orderItemService.updateOrderItem(1L, 5));

        Mockito.verify(orderItemRepository, Mockito.times(1)).getOrderItemById(1L);
        Mockito.verify(orderRepository, Mockito.times(1)).findById(22L);
        Mockito.verify(productRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(orderItemRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void updateOrderItem_orderNotPending() {
        Product product = Mockito.mock(Product.class);
        Order order = Mockito.mock(Order.class);

        Mockito.when(order.getId()).thenReturn(22L);
        Mockito.when(order.getStatus()).thenReturn(OrderStatus.DELIVERED);

        OrderItem orderItem = new OrderItem(1L, product, order, 5, 4.0);

        Mockito.when(orderItemRepository.getOrderItemById(1L)).thenReturn(Optional.of(orderItem));
        Mockito.when(orderRepository.findById(22L)).thenReturn(Optional.of(order));

        Assertions.assertThrows(InvalidOrderStateException.class, () -> orderItemService.updateOrderItem(1L, 5));

        Mockito.verify(orderItemRepository, Mockito.times(1)).getOrderItemById(1L);
        Mockito.verify(orderRepository, Mockito.times(1)).findById(22L);
        Mockito.verify(productRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(orderItemRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void updateOrderItem_productNotFound() {
        Product product = Mockito.mock(Product.class);
        Mockito.when(product.getId()).thenReturn(11L);

        Order order = Mockito.mock(Order.class);
        Mockito.when(order.getId()).thenReturn(22L);
        Mockito.when(order.getStatus()).thenReturn(OrderStatus.PENDING);

        OrderItem orderItem = new OrderItem(1L, product, order, 5, 4.0);

        Mockito.when(orderItemRepository.getOrderItemById(1L)).thenReturn(Optional.of(orderItem));
        Mockito.when(orderRepository.findById(22L)).thenReturn(Optional.of(order));
        Mockito.when(productRepository.findById(11L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> orderItemService.updateOrderItem(1L, 5));

        Mockito.verify(orderItemRepository, Mockito.times(1)).getOrderItemById(1L);
        Mockito.verify(orderRepository, Mockito.times(1)).findById(22L);
        Mockito.verify(productRepository, Mockito.times(1)).findById(11L);
        Mockito.verify(orderItemRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void updateOrderItem_productOutOfStock() {
        Product product = Mockito.mock(Product.class);
        Mockito.when(product.getId()).thenReturn(11L);
        Mockito.when(product.getStock()).thenReturn(4);

        Order order = Mockito.mock(Order.class);
        Mockito.when(order.getId()).thenReturn(22L);
        Mockito.when(order.getStatus()).thenReturn(OrderStatus.PENDING);

        OrderItem orderItem = new OrderItem(1L, product, order, 5, 4.0);

        Mockito.when(orderItemRepository.getOrderItemById(1L)).thenReturn(Optional.of(orderItem));
        Mockito.when(orderRepository.findById(22L)).thenReturn(Optional.of(order));
        Mockito.when(productRepository.findById(11L)).thenReturn(Optional.of(product));

        Assertions.assertThrows(OutOfStockException.class, () -> orderItemService.updateOrderItem(1L, 5));

        Mockito.verify(orderItemRepository, Mockito.times(1)).getOrderItemById(1L);
        Mockito.verify(orderRepository, Mockito.times(1)).findById(22L);
        Mockito.verify(productRepository, Mockito.times(1)).findById(11L);
        Mockito.verify(orderItemRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void updateOrderItem() {
        Product product = Mockito.mock(Product.class);
        Mockito.when(product.getId()).thenReturn(11L);
        Mockito.when(product.getStock()).thenReturn(10);

        Order order = Mockito.mock(Order.class);
        Mockito.when(order.getId()).thenReturn(22L);
        Mockito.when(order.getStatus()).thenReturn(OrderStatus.PENDING);

        OrderItem orderItem = new OrderItem(1L, product, order, 5, 4.0);

        Mockito.when(orderItemRepository.getOrderItemById(1L)).thenReturn(Optional.of(orderItem));
        Mockito.when(orderRepository.findById(22L)).thenReturn(Optional.of(order));
        Mockito.when(productRepository.findById(11L)).thenReturn(Optional.of(product));

        OrderItem expectedOrderItem = new OrderItem(1L, product, order, 10, 4.0);

        Mockito.when(orderItemRepository.save(Mockito.any())).thenReturn(expectedOrderItem);

        OrderItem response = orderItemService.updateOrderItem(1L, 10);

        Assertions.assertEquals(expectedOrderItem, response);

        Mockito.verify(orderItemRepository, Mockito.times(1)).getOrderItemById(1L);
        Mockito.verify(orderRepository, Mockito.times(1)).findById(22L);
        Mockito.verify(productRepository, Mockito.times(1)).findById(11L);
        Mockito.verify(orderItemRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void deleteOrderItem_OrderItemNotFound() {
        Mockito.when(orderItemRepository.getOrderItemById(1L)).thenReturn(Optional.empty());

        Assertions.assertFalse(orderItemService.deleteOrderItem(1L));

        Mockito.verify(orderItemRepository, Mockito.times(1)).getOrderItemById(1L);
        Mockito.verify(orderRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(orderItemRepository, Mockito.never()).delete(Mockito.any());
    }

    @Test
    void deleteOrderItem_OrderNotFound() {
        Product product = Mockito.mock(Product.class);
        Order order = Mockito.mock(Order.class);

        Mockito.when(order.getId()).thenReturn(22L);

        OrderItem orderItem = new OrderItem(1L, product, order, 5, 4.0);

        Mockito.when(orderItemRepository.getOrderItemById(1L)).thenReturn(Optional.of(orderItem));
        Mockito.when(orderRepository.findById(22L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> orderItemService.deleteOrderItem(1L));

        Mockito.verify(orderItemRepository, Mockito.times(1)).getOrderItemById(1L);
        Mockito.verify(orderRepository, Mockito.times(1)).findById(22L);
        Mockito.verify(orderItemRepository, Mockito.never()).delete(Mockito.any());
    }

    @Test
    void deleteOrderItem_OrderNotPending() {
        Product product = Mockito.mock(Product.class);
        Order order = Mockito.mock(Order.class);

        Mockito.when(order.getId()).thenReturn(22L);
        Mockito.when(order.getStatus()).thenReturn(OrderStatus.DELIVERED);

        OrderItem orderItem = new OrderItem(1L, product, order, 5, 4.0);

        Mockito.when(orderItemRepository.getOrderItemById(1L)).thenReturn(Optional.of(orderItem));
        Mockito.when(orderRepository.findById(22L)).thenReturn(Optional.of(order));

        Assertions.assertThrows(InvalidOrderStateException.class, () -> orderItemService.deleteOrderItem(1L));

        Mockito.verify(orderItemRepository, Mockito.times(1)).getOrderItemById(1L);
        Mockito.verify(orderRepository, Mockito.times(1)).findById(22L);
        Mockito.verify(orderItemRepository, Mockito.never()).delete(Mockito.any());
    }

    @Test
    void deleteOrderItem() {
        Product product = Mockito.mock(Product.class);
        Order order = Mockito.mock(Order.class);

        Mockito.when(order.getId()).thenReturn(22L);
        Mockito.when(order.getStatus()).thenReturn(OrderStatus.PENDING);

        OrderItem orderItem = new OrderItem(1L, product, order, 5, 4.0);

        Mockito.when(orderItemRepository.getOrderItemById(1L)).thenReturn(Optional.of(orderItem));
        Mockito.when(orderRepository.findById(22L)).thenReturn(Optional.of(order));

        boolean response = orderItemService.deleteOrderItem(1L);

        Assertions.assertTrue(response);

        Mockito.verify(orderItemRepository, Mockito.times(1)).getOrderItemById(1L);
        Mockito.verify(orderRepository, Mockito.times(1)).findById(22L);
        Mockito.verify(orderItemRepository, Mockito.times(1)).delete(orderItem);
    }
}