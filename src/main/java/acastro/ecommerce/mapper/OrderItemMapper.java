package acastro.ecommerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import acastro.ecommerce.dto.OrderItemResponseDto;
import acastro.ecommerce.model.OrderItem;

@Mapper
public interface OrderItemMapper {

    OrderItemMapper mapper = Mappers.getMapper(OrderItemMapper.class);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "orderId", source = "order.id")
    OrderItemResponseDto toDto(OrderItem orderItem);

}
