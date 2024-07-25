package acastro.ecommerce.service.impl;

import java.util.Optional;
import java.util.List;

import org.springframework.stereotype.Service;

import acastro.ecommerce.exception.NotFoundException;
import acastro.ecommerce.model.Product;
import acastro.ecommerce.repository.ProductRepository;
import acastro.ecommerce.service.ProductService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;


    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long productId, Product newProductData) {
        final Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        product.setName(newProductData.getName());
        product.setDescription(newProductData.getDescription());
        product.setPrice(newProductData.getPrice());
        product.setStock(newProductData.getStock());

        return productRepository.save(product);
    }

    @Override
    public boolean deleteProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty() || product.get().isDiscontinued()) {
            return false;
        }
        product.get().setDiscontinued(true);
        productRepository.save(product.get());
        return true;
    }
}
