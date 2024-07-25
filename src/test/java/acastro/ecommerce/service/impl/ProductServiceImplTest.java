package acastro.ecommerce.service.impl;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import acastro.ecommerce.exception.NotFoundException;
import acastro.ecommerce.model.Product;
import acastro.ecommerce.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    private final Product PRODUCT = new Product(1L, "Product 1", "Description 1", 10.0, 10, false);

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void getAllProducts() {
        Mockito.when(productRepository.findAll()).thenReturn(List.of(PRODUCT));
        List<Product> expectedResponse = List.of(PRODUCT);

        List<Product> response = productService.getAllProducts();

        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    void getProductById() {
        Mockito.when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.of(PRODUCT));

        Optional<Product> response = productService.getProductById(PRODUCT.getId());

        Assertions.assertTrue(response.isPresent());
        Assertions.assertEquals(PRODUCT, response.get());
    }

    @Test
    void createProduct() {
        Long createdId = 33L;
        Product newProduct = new Product(null, "New product 1", "123213", 10.0, 10, false);
        Product expectedResponse = new Product(createdId, "New product 1", "123213", 10.0, 10, false);

        Mockito.when(productRepository.save(newProduct)).thenAnswer(invocation -> {
            Product save = invocation.getArgument(0);
            save.setId(createdId);
            return invocation.getArgument(0);
        });

        Product response = productService.createProduct(newProduct);

        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    void updateProduct() {
        Product productNewData = new Product(null, "Updated name", "123213", 99.9, 250, false);

        Mockito.when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.of(PRODUCT));
        Mockito.when(productRepository.save(PRODUCT)).thenAnswer(invocation -> invocation.getArgument(0));

        Product expectedResponse = new Product(PRODUCT.getId(), "Updated name", "123213", 99.9, 250, false);

        Product response = productService.updateProduct(PRODUCT.getId(), productNewData);

        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    void updateProduct_notFound() {
        Product productNewData = new Product(null, "Updated name", "123213", 99.9, 250, false);

        Mockito.when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> productService.updateProduct(PRODUCT.getId(), productNewData));

        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void deleteProduct() {
        Product toDelete = new Product(1L, "Product 1", "Description 1", 10.0, 10, false);
        Mockito.when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.of(toDelete));
        Mockito.when(productRepository.save(toDelete)).thenAnswer(invocation -> invocation.getArgument(0));

        boolean response = productService.deleteProduct(PRODUCT.getId());

        Assertions.assertTrue(response);

        // verify that the product was set as discontinued
        Assertions.assertTrue(toDelete.isDiscontinued());
    }

    @Test
    void deleteProduct_discontinued() {
        Product toDelete = new Product(1L, "Product 1", "Description 1", 10.0, 10, true);
        Mockito.when(productRepository.findById(toDelete.getId())).thenReturn(Optional.of(toDelete));

        Assertions.assertFalse(productService.deleteProduct(toDelete.getId()));

        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void deleteProduct_notFound() {
        Mockito.when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.empty());

        Assertions.assertFalse(productService.deleteProduct(PRODUCT.getId()));

        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any());
    }
}