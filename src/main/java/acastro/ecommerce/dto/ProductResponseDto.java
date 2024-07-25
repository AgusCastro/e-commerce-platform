package acastro.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProductResponse")
public record ProductResponseDto(
        Long id,
        String name,
        String description,
        Double price,
        Integer stock,
        boolean discontinued
) {
}
