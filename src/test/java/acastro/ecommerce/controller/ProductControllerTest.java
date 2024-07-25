package acastro.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import acastro.ecommerce.dto.ProductRequestDto;
import acastro.ecommerce.dto.ProductResponseDto;
import acastro.ecommerce.exception.NotFoundException;
import acastro.ecommerce.mapper.ProductMapper;
import acastro.ecommerce.model.Product;
import acastro.ecommerce.service.ProductService;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private final Product PRODUCT = new Product(1L, "Product 1", "Description 1", 10.0, 10, true);

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @Test
    void getProducts() {
        List<Product> products = List.of(PRODUCT,
                new Product(2L, "Product 2", "Description 2", 20.0, 10, true));
        Mockito.when(productService.getAllProducts()).thenReturn(products);
        List<ProductResponseDto> expectedResponse = products.stream().map(ProductMapper.mapper::toDto).toList();

        List<ProductResponseDto> response = productController.getProducts();

        Assertions.assertEquals(expectedResponse, response);

        Mockito.verify(productService, Mockito.times(1)).getAllProducts();
    }

    @Test
    void getProduct() {
        Mockito.when(productService.getProductById(PRODUCT.getId())).thenReturn(Optional.of(PRODUCT));
        ProductResponseDto expectedResponse = ProductMapper.mapper.toDto(PRODUCT);

        ProductResponseDto response = productController.getProduct(PRODUCT.getId());

        Assertions.assertEquals(expectedResponse, response);

        Mockito.verify(productService, Mockito.times(1)).getProductById(PRODUCT.getId());
    }

    @Test
    void getProduct_NotFoundError() {
        Mockito.when(productService.getProductById(PRODUCT.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> productController.getProduct(PRODUCT.getId()));

        Mockito.verify(productService, Mockito.times(1)).getProductById(PRODUCT.getId());
    }

    @Test
    void createProduct() {
        Product product = new Product(null, "Product 3", "Description 3", 30.0, 10, true);
        ProductResponseDto expectedResponse = ProductMapper.mapper.toDto(product);
        Mockito.when(productService.createProduct(Mockito.any(Product.class))).thenReturn(product);

        ProductResponseDto response = productController.createProduct(
                new ProductRequestDto(product.getName(), product.getDescription(), product.getPrice(), product.getStock())
        );

        Assertions.assertEquals(expectedResponse, response);

        Mockito.verify(productService, Mockito.times(1)).createProduct(Mockito.any(Product.class));
    }

    @Test
    void updateProduct() {
        Product product = new Product(PRODUCT.getId(), "Product 3", "Description 3", 30.0, 10, true);
        ProductResponseDto expectedResponse = ProductMapper.mapper.toDto(product);
        Mockito.when(productService.updateProduct(Mockito.eq(PRODUCT.getId()), Mockito.any(Product.class))).thenReturn(product);

        ProductResponseDto response = productController.updateProduct(PRODUCT.getId(),
                new ProductRequestDto(product.getName(), product.getDescription(), product.getPrice(), product.getStock()));

        Assertions.assertEquals(expectedResponse, response);

        Mockito.verify(productService, Mockito.times(1)).updateProduct(Mockito.eq(PRODUCT.getId()), Mockito.any(Product.class));
    }

    @Test
    void deleteProduct() {
        Mockito.when(productService.deleteProduct(PRODUCT.getId())).thenReturn(true);

        Boolean response = productController.deleteProduct(PRODUCT.getId());

        Assertions.assertTrue(response);

        Mockito.verify(productService, Mockito.times(1)).deleteProduct(PRODUCT.getId());
    }
}