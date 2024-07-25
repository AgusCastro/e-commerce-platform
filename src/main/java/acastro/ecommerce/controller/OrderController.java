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

import acastro.ecommerce.dto.CreateOrderRequestDto;
import acastro.ecommerce.dto.OrderResponseDto;
import acastro.ecommerce.dto.OrderUpdateRequestDto;
import acastro.ecommerce.exception.NotFoundException;
import acastro.ecommerce.mapper.OrderMapper;
import acastro.ecommerce.model.Order;
import acastro.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Tag(name = "Orders", description = "Operations related to orders")
@RestController
@RequestMapping("/orders")
@AllArgsConstructor
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Get all orders")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of orders",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = OrderResponseDto.class)
                )),
    })
    @GetMapping
    public List<OrderResponseDto> getOrders() {
        return orderService.getAllOrders().stream().map(OrderMapper.mapper::toDto).toList();
    }

    @Operation(summary = "Get an order by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order found",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = OrderResponseDto.class)
                )),
        @ApiResponse(responseCode = "404", description = "Order not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @GetMapping("/{id}")
    public OrderResponseDto getOrder(@Parameter(description = "Id of the order to search", required = true)
            @PathVariable("id") Long id) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        return OrderMapper.mapper.toDto(order);
    }

    @Operation(summary = "Create a new order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order created",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = OrderResponseDto.class)
                )),
    })
    @PostMapping
    public OrderResponseDto createOrder(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Order to be created",
                required = true,
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = CreateOrderRequestDto.class)
                ))
            @org.springframework.web.bind.annotation.RequestBody
            @Valid CreateOrderRequestDto order) {
        Order created = orderService.createOrder(order.customerName(), order.deliveryAddress());
        return OrderMapper.mapper.toDto(created);
    }

    @Operation(summary = "Update an order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order updated",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = OrderResponseDto.class)
                )),
        @ApiResponse(responseCode = "404", description = "Order not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")    
    public OrderResponseDto updateOrder(
            @Parameter(description = "Id of the order to update", required = true)
            @PathVariable("id") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Order to be updated", required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderUpdateRequestDto.class)
                    ))
            @org.springframework.web.bind.annotation.RequestBody
            @Valid OrderUpdateRequestDto orderRequest) {
        Order order = OrderMapper.mapper.toEntity(orderRequest);

        Order updated = orderService.updateOrder(id, order);
        return OrderMapper.mapper.toDto(updated);
    }

    @Operation(summary = "Delete an order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order deleted"),
        @ApiResponse(responseCode = "404", description = "Order not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
    })
    @DeleteMapping("/{id}")
    public Boolean deleteOrder(
            @Parameter(description = "Id of the order to delete", required = true)
            @PathVariable("id") Long id) {
        return orderService.deleteOrder(id);
    }
}
