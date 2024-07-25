package acastro.ecommerce.repository;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import acastro.ecommerce.model.Product;

public interface ProductRepository extends ListCrudRepository<Product, Long> {

    List<Product> findAll();
}
