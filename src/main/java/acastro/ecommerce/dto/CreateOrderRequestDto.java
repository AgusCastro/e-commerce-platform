package acastro.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;

@Schema(name = "CreateOrderRequest")
public record CreateOrderRequestDto(

        @NotNull(message = "customerName must not be null")
        @Length(min = 1, max = 100, message = "customerName must be between 1 and 100 characters")
        String customerName,

        @NotNull(message = "deliveryAddress must not be null")
        @Length(min = 1, max = 250, message = "deliveryAddress must be between 1 and 250 characters")
        String deliveryAddress
) {
}
