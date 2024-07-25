package acastro.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "OrderItemResponse")
public record OrderItemResponseDto(
        Long id,
        Long productId,
        Long orderId,
        Integer quantity,
        Double unitPrice,
        Double total
) {
}
