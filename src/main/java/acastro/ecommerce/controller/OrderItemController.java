package acastro.ecommerce.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import acastro.ecommerce.dto.OrderItemRequestDto;
import acastro.ecommerce.dto.OrderItemResponseDto;
import acastro.ecommerce.dto.OrderItemUpdateRequestDto;
import acastro.ecommerce.exception.NotFoundException;
import acastro.ecommerce.mapper.OrderItemMapper;
import acastro.ecommerce.model.OrderItem;
import acastro.ecommerce.service.OrderItemService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Tag(name = "Order Items", description = "Operations related to order items")
@RestController
@RequestMapping("/order-items")
@AllArgsConstructor
@CrossOrigin
public class OrderItemController {

    private final OrderItemService orderItemService;

    @Operation(summary = "Get all order items")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of order items",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = OrderItemResponseDto.class)
                )),
    })
    @GetMapping
    public List<OrderItemResponseDto> getOrderItems() {
        return orderItemService.getAllOrderItems().stream().map(OrderItemMapper.mapper::toDto).toList();
    }

    @Operation(summary = "Get an order item by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order item found",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = OrderItemResponseDto.class)
                )),
        @ApiResponse(responseCode = "404", description = "Order item not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
    })
    @GetMapping("/{id}")
    public OrderItemResponseDto getOrderItem(@PathVariable("id") Long id) {
        OrderItem orderItem = orderItemService.getOrderItemById(id)
                .orElseThrow(() -> new NotFoundException("Order item not found"));
        return OrderItemMapper.mapper.toDto(orderItem);
    }

    @Operation(summary = "Create an order item")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order item created",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = OrderItemResponseDto.class)
                )),
        @ApiResponse(responseCode = "404", description = "Order or product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input / Invalid order state / Product out of stock / Item already exists in order"),
    })
    @PostMapping
    public OrderItemResponseDto createOrderItem(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Order item to create", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderItemRequestDto.class))
            )
            @org.springframework.web.bind.annotation.RequestBody
            @Valid OrderItemRequestDto orderItemRequest) {
        OrderItem orderItem = orderItemService.createOrderItem(orderItemRequest.orderId(), orderItemRequest.productId(),
                orderItemRequest.quantity());
        return OrderItemMapper.mapper.toDto(orderItem);
    }

    @Operation(summary = "Update an order item")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order item updated",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = OrderItemResponseDto.class)
                )),
        @ApiResponse(responseCode = "404", description = "Order item not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input / Invalid order state / Product out of stock"),
    })
    @PutMapping("/{id}")    
    public OrderItemResponseDto updateOrderItem(
            @Parameter(description = "Id of the order item to delete", required = true)
            @PathVariable("id") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Order item data to update", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderItemUpdateRequestDto.class))
            )
            @org.springframework.web.bind.annotation.RequestBody
            @Valid OrderItemUpdateRequestDto updateRequest) {
        OrderItem updatedOrderItem =orderItemService.updateOrderItem(id, updateRequest.quantity());
        return OrderItemMapper.mapper.toDto(updatedOrderItem);
    }

    @Operation(summary = "Delete an order item")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order item deleted"),
        @ApiResponse(responseCode = "404", description = "Order item not found"),
    })
    @DeleteMapping("/{id}")
    public Boolean deleteOrderItem(
            @Parameter(description = "Id of the order item to delete", required = true)
            @PathVariable("id") Long id) {
        return orderItemService.deleteOrderItem(id);
    }
}
