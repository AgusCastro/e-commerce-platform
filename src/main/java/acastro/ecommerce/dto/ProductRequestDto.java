package acastro.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Schema(name = "ProductRequest")
public record ProductRequestDto(

        @NotBlank(message = "Name must not be blank")
        @Size(min = 1, max = 40, message = "Name must be between 1 and 40 characters")
        String name,

        @NotNull(message = "Description must not be null")
        @Size(min = 0, max = 100, message = "Description must not exceed 100 characters")
        String description,

        @NotNull(message = "Price must not be null")
        @Positive(message = "Price must be positive")
        Double price,

        @NotNull(message = "Price must not be null")
        @PositiveOrZero(message = "Stock must be positive or zero")
        Integer stock
) {
}
