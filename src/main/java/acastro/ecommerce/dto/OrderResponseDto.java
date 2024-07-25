package acastro.ecommerce.dto;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "OrderResponse")
public record OrderResponseDto(
        Long id,
        LocalDate orderDate,
        String customerName,
        String deliveryAddress,
        LocalDate deliveryDate,
        String status,
        List<OrderItemResponseDto> items
) {
}
