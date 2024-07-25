package acastro.ecommerce.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import acastro.ecommerce.dto.ProductRequestDto;
import acastro.ecommerce.dto.ProductResponseDto;
import acastro.ecommerce.exception.NotFoundException;
import acastro.ecommerce.mapper.ProductMapper;
import acastro.ecommerce.model.Product;
import acastro.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Tag(name = "Products", description = "Operations related to products")
@RestController
@RequestMapping("/products")
@AllArgsConstructor
@CrossOrigin
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get all products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of products",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ProductResponseDto.class)
                )),
    })
    @GetMapping
    public List<ProductResponseDto> getProducts() {
        return productService.getAllProducts().stream()
                .map(ProductMapper.mapper::toDto)
                .toList();
    }

    @Operation(summary = "Get a product by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product found",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ProductResponseDto.class)
                )),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content),
    })
    @GetMapping("/{id}")
    public ProductResponseDto getProduct( @Parameter(description = "Id of the product to search", required = true)
            @PathVariable("id") Long id) {
        final Product product = productService.getProductById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        return ProductMapper.mapper.toDto(product);
    }

    @Operation(summary = "Create a new product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product created",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ProductResponseDto.class)
                )),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
    })
    @PostMapping
    public ProductResponseDto createProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Product data to create",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductRequestDto.class)
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody
            @Valid ProductRequestDto productRequest) {
        final Product product = ProductMapper.mapper.toEntity(productRequest);

        final Product createdProduct = productService.createProduct(product);

        return ProductMapper.mapper.toDto(createdProduct);
    }

    @Operation(summary = "Update a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ProductResponseDto.class)
                )),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content),
    })
    @PutMapping("/{id}")
    public ProductResponseDto updateProduct(
            @Parameter(description = "Id of the product to update", required = true)
            @PathVariable("id") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product data to update", required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductRequestDto.class)
                    ))
            @org.springframework.web.bind.annotation.RequestBody
            @Valid ProductRequestDto productRequest) {
        final Product productData = ProductMapper.mapper.toEntity(productRequest);
        final Product savedProduct = productService.updateProduct(id, productData);
        return ProductMapper.mapper.toDto(savedProduct);
    }

    @Operation(summary = "Delete a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product deleted")
    })
    @DeleteMapping("/{id}")
    public Boolean deleteProduct( @Parameter(description = "Id of the product to delete", required = true)
            @PathVariable("id") Long id) {
        return productService.deleteProduct(id);
    }
}
