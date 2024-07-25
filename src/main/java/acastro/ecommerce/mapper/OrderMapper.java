package acastro.ecommerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import acastro.ecommerce.model.Order;
import acastro.ecommerce.dto.OrderResponseDto;
import acastro.ecommerce.dto.OrderUpdateRequestDto;

@Mapper(uses= {OrderItemMapper.class})
public interface OrderMapper {

    OrderMapper mapper = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "orderDate", source = "creationDate")
    @Mapping(target = "items", source = "items")
    OrderResponseDto toDto(Order order);

    Order toEntity(OrderUpdateRequestDto orderRequest);
}
