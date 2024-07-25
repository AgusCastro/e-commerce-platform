package acastro.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(name = "OrderItemRequest")
public record OrderItemRequestDto(
        @NotNull(message = "orderId must not be null")
        Long orderId,

        @NotNull(message = "productId must not be null")
        Long productId,

        @NotNull(message = "quantity must not be null")
        @Positive(message = "quantity must be positive")
        Integer quantity
) {
}
