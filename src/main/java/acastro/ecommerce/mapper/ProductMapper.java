package acastro.ecommerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import acastro.ecommerce.dto.ProductRequestDto;
import acastro.ecommerce.dto.ProductResponseDto;
import acastro.ecommerce.model.Product;

@Mapper
public interface ProductMapper {

    ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    ProductResponseDto toDto(Product product);

    Product toEntity(ProductRequestDto product);

}
