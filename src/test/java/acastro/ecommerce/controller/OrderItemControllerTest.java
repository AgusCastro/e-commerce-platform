package acastro.ecommerce.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import acastro.ecommerce.exception.NotFoundException;
import acastro.ecommerce.dto.OrderItemRequestDto;
import acastro.ecommerce.dto.OrderItemResponseDto;
import acastro.ecommerce.dto.OrderItemUpdateRequestDto;
import acastro.ecommerce.model.Order;
import acastro.ecommerce.model.OrderItem;
import acastro.ecommerce.model.Product;
import acastro.ecommerce.service.OrderItemService;

@ExtendWith(MockitoExtension.class)
class OrderItemControllerTest {

    private static final BigDecimal UNIT_PRICE = BigDecimal.valueOf(10.1);
    private final Product PRODUCT = new Product(1L, "Product 1", "", BigDecimal.valueOf(100), 2, false);
    private final Long ORDER_ID = 1L;
    private final Order order = Mockito.mock(Order.class);


    @Mock
    private OrderItemService orderItemService;

    @InjectMocks
    private OrderItemController orderItemController;

    @BeforeEach
    void setUp() {
        Mockito.when(order.getId()).thenReturn(ORDER_ID);
    }

    @Test
    void getOrderItems() {
        Product customProduct = new Product(222L, "Product 2", "", BigDecimal.valueOf(20), 2, false);

        List<OrderItem> orderItems = List.of(new OrderItem(22L, PRODUCT, order, 1, UNIT_PRICE),
                new OrderItem(33L, customProduct, order, 4, UNIT_PRICE));
        Mockito.when(orderItemService.getAllOrderItems()).thenReturn(orderItems);

        List<OrderItemResponseDto> response = orderItemController.getOrderItems();

        Assertions.assertEquals(orderItems.size(), response.size());
        Mockito.verify(orderItemService).getAllOrderItems();
    }

    @Test
    void getOrderItem() {
        OrderItem orderItem = new OrderItem(22L, PRODUCT, order, 1, UNIT_PRICE);

        Mockito.when(orderItemService.getOrderItemById(1L)).thenReturn(Optional.of(orderItem));

        OrderItemResponseDto response = orderItemController.getOrderItem(1L);

        Assertions.assertNotNull(response);
        Mockito.verify(orderItemService).getOrderItemById(1L);
    }

    @Test
    void getOrderItem_NotFoundError() {

        Mockito.when(orderItemService.getOrderItemById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> orderItemController.getOrderItem(1L));

        Mockito.verify(orderItemService).getOrderItemById(1L);
    }

    @Test
    void createOrderItem() {
        OrderItemRequestDto requestDto = new OrderItemRequestDto(ORDER_ID, 1L, 10);
        OrderItem orderItem = new OrderItem(22L, PRODUCT, order, 10, BigDecimal.valueOf(50.00));

        Mockito.when(orderItemService.createOrderItem(requestDto.orderId(), requestDto.productId(), requestDto.quantity())).thenReturn(orderItem);

        OrderItemResponseDto response = orderItemController.createOrderItem(requestDto);

        Assertions.assertNotNull(response);
        Mockito.verify(orderItemService).createOrderItem(requestDto.orderId(), requestDto.productId(), requestDto.quantity());
    }

    @Test
    void updateOrderItem() {
        OrderItemUpdateRequestDto updateRequestDto = new OrderItemUpdateRequestDto(25);

        OrderItem updatedOrderItem = new OrderItem(22L, PRODUCT, order, 25, BigDecimal.valueOf(50));
        Mockito.when(orderItemService.updateOrderItem(1L, updateRequestDto.quantity())).thenReturn(updatedOrderItem);

        OrderItemResponseDto response = orderItemController.updateOrderItem(1L, updateRequestDto);

        Assertions.assertNotNull(response);
        Mockito.verify(orderItemService).updateOrderItem(1L, updateRequestDto.quantity());
    }

    @Test
    void deleteOrderItem() {
        Mockito.when(orderItemService.deleteOrderItem(1L)).thenReturn(true);

        Boolean response = orderItemController.deleteOrderItem(1L);

        Assertions.assertTrue(response);
        Mockito.verify(orderItemService).deleteOrderItem(1L);
    }
}