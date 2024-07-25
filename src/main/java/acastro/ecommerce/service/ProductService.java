package acastro.ecommerce.service;

import java.util.Optional;
import java.util.List;

import acastro.ecommerce.model.Product;

public interface ProductService {

    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    Product createProduct(Product product);
    Product updateProduct(Long productId, Product newProductData);
    boolean deleteProduct(Long id);

}
