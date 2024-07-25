package acastro.ecommerce.exception;

public class OutOfStockException extends RuntimeException {

    public OutOfStockException(Long productId) {
        super("Product with id " + productId + " is out of stock!");
    }
}
