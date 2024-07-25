package acastro.ecommerce.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

import org.hibernate.validator.constraints.Length;

import acastro.ecommerce.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

@Schema(name = "OrderUpdateRequest")
public record OrderUpdateRequestDto(

        LocalDate deliveryDate,

        @NotNull(message = "Delivery address must not be null")
        @Length(min = 1, max = 40, message = "Name must be between 1 and 40 characters")
        String deliveryAddress,

        @NotNull(message = "Status must not be null")
        OrderStatus status
) {
}
