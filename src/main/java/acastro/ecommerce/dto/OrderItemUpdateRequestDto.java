package acastro.ecommerce.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;

@Schema(name = "OrderItemUpdateRequest")
public record OrderItemUpdateRequestDto(
        @NotNull(message = "quantity must not be null")
        @Parameter(description = "quantity of the product", required = true)
        Integer quantity
) {
}
