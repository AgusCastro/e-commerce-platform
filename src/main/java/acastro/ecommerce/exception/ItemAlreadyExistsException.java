package acastro.ecommerce.exception;

public class ItemAlreadyExistsException extends RuntimeException {

    public ItemAlreadyExistsException(Long productId) {
        super("Item with product id " + productId + " already exists.");
    }
}
