package acastro.ecommerce.config;

import java.util.Objects;
import java.util.Optional;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import acastro.ecommerce.error.CustomErrorResponse;
import acastro.ecommerce.exception.NotFoundException;
import acastro.ecommerce.exception.OutOfStockException;

@RestControllerAdvice
public class GlobalRestControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Optional<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .reduce((error1, error2) -> error1 + ", " + error2);

        CustomErrorResponse errorResponse = new CustomErrorResponse(HttpStatus.BAD_REQUEST, errors.orElseGet(() -> ex.getBody().getDetail()), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationExceptions(NotFoundException ex, WebRequest request) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationExceptions(OutOfStockException ex, WebRequest request) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationExceptions(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String message = Objects.nonNull(ex.getRequiredType()) ?
                "The parameter '" + ex.getName() + "' should be of type " + ex.getRequiredType().getSimpleName()
                : "The parameter '" + ex.getName() + "' is invalid";
        CustomErrorResponse errorResponse = new CustomErrorResponse(HttpStatus.BAD_REQUEST, message, request);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
