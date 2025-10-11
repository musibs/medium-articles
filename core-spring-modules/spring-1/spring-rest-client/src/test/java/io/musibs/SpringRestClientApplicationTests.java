package io.musibs;

import io.musibs.entity.Product;
import io.musibs.exception.AccessDeniedException;
import io.musibs.exception.ProductNotFoundException;
import io.musibs.exception.ServiceException;
import io.musibs.interceptors.LoggingInterceptor;
import io.musibs.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SpringRestClientApplicationTests {

    private RestClient restClient;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        // Clear database before each test
        productRepository.deleteAll();

        // Create RestClient instance
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Test
    void testGetAllProducts() {
        // Arrange: Create test data
        Product product1 = new Product("Laptop", new BigDecimal("999.99"), "Electronics", 10);
        Product product2 = new Product("Mouse", new BigDecimal("29.99"), "Electronics", 50);
        productRepository.saveAll(List.of(product1, product2));

        // Act: Make GET request
        List<Product> products = restClient.get()
                .uri("/api/products")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        // Assert
        assertNotNull(products);
        assertEquals(2, products.size());
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Laptop")));
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Mouse")));
    }

    @Test
    void testGetProductById() {
        // Arrange
        Product saved = productRepository.save(
                new Product("Keyboard", new BigDecimal("79.99"), "Electronics", 25)
        );

        // Act: URI template with path variable
        Product product = restClient.get()
                .uri("/api/products/{id}", saved.getId())
                .retrieve()
                .body(Product.class);

        // Assert
        assertNotNull(product);
        assertEquals("Keyboard", product.getName());
        assertEquals(new BigDecimal("79.99"), product.getPrice());
        assertEquals("Electronics", product.getCategory());
    }

    @Test
    void testRequestParameters() {

        Product product1 = new Product("Keyboard", new BigDecimal("79.99"), "Electronics", 25);
        Product product2 = new Product("Mouse", new BigDecimal("29.99"), "Electronics", 50);
        Product product3 = new Product("Jeans", new BigDecimal("99.99"), "Clothing", 20);

        productRepository.saveAll(List.of(product1, product2, product3));

        List<Product> fetchedProducts = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/products/search")
                        .queryParam("name", "Keyboard")
                        .queryParam("category", "Electronics")
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        assertEquals(1, fetchedProducts.size());
    }

    @Test
    void testDynamicQueryParameters() {
        // Arrange
        productRepository.save(
                new Product("Monitor", new BigDecimal("299.99"), "Electronics", 15)
        );

        Map<String, String> params = new HashMap<>();
        params.put("name", "Monitor");
        params.put("category", "Electronics");

        // Act: Build URI dynamically
        List<Product> results = restClient.get()
                .uri(uriBuilder -> {
                    UriBuilder builder = uriBuilder.path("/api/products/search");
                    params.forEach(builder::queryParam);
                    return builder.build();
                })
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void testGetWithCustomHeaders() {
        // Arrange
        Product saved = productRepository.save(
                new Product("Headphones", new BigDecimal("149.99"), "Electronics", 20)
        );

        // Act: Add custom headers
        ResponseEntity<Product> response = restClient.get()
                .uri("/api/products/{id}", saved.getId())
                .header("X-Request-ID", UUID.randomUUID().toString())
                .header("X-Client-Version", "1.0")
                .retrieve()
                .toEntity(Product.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Headphones", response.getBody().getName());
    }

    @Test
    void testCreateProduct() {
        // Arrange: Create product to send
        Product newProduct = new Product(
                "Webcam",
                new BigDecimal("129.99"),
                "Electronics",
                15
        );

        // Act: POST request with JSON body
        Product created = restClient.post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .body(newProduct)
                .retrieve()
                .body(Product.class);

        // Assert
        assertNotNull(created);
        assertNotNull(created.getId()); // ID should be generated
        assertEquals("Webcam", created.getName());
        assertEquals(new BigDecimal("129.99"), created.getPrice());

        // Verify it's actually saved
        assertTrue(productRepository.existsById(created.getId()));
    }

    @Test
    void testCreateProductWithResponseEntity() {
        // Arrange
        Product newProduct = new Product(
                "USB Cable",
                new BigDecimal("9.99"),
                "Accessories",
                100
        );

        // Act: Get full ResponseEntity
        ResponseEntity<Product> response = restClient.post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .body(newProduct)
                .retrieve()
                .toEntity(Product.class);

        // Assert: Check status code
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Assert: Check headers
        assertNotNull(response.getHeaders());

        // Assert: Check body
        Product created = response.getBody();
        assertNotNull(created);
        assertEquals("USB Cable", created.getName());
    }

    @Test
    void testCreateProductWithTypeReference() {
        // Useful when the response is a generic type
        Product newProduct = new Product(
                "Monitor Stand",
                new BigDecimal("49.99"),
                "Accessories",
                25
        );

        // Act: Using ParameterizedTypeReference for type safety
        Product created = restClient.post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .body(newProduct, new ParameterizedTypeReference<>() {
                })
                .retrieve()
                .body(Product.class);

        // Assert
        assertNotNull(created);
        assertEquals("Monitor Stand", created.getName());
    }

    @Test
    void testUpdateProduct() {
        // Arrange: Create existing product
        Product existing = productRepository.save(
                new Product("Old Name", new BigDecimal("99.99"), "Electronics", 10)
        );

        // Prepare update
        Product update = new Product(
                "Updated Name",
                new BigDecimal("149.99"),
                "Premium Electronics",
                15
        );

        // Act: PUT request to update
        Product updated = restClient.put()
                .uri("/api/products/{id}", existing.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(update)
                .retrieve()
                .body(Product.class);

        // Assert
        assertNotNull(updated);
        assertEquals(existing.getId(), updated.getId()); // ID unchanged
        assertEquals("Updated Name", updated.getName());
        assertEquals(new BigDecimal("149.99"), updated.getPrice());
        assertEquals("Premium Electronics", updated.getCategory());
        assertEquals(15, updated.getStockQuantity());
    }

    @Test
    void testPartialUpdateProductStock() {
        // Arrange: Create product
        Product existing = productRepository.save(
                new Product("Laptop", new BigDecimal("999.99"), "Electronics", 10)
        );

        // Prepare partial update (only stock quantity)
        Map<String, Integer> stockUpdate = Map.of("stockQuantity", 50);

        // Act: PATCH request
        Product updated = restClient.patch()
                .uri("/api/products/{id}/stock", existing.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(stockUpdate)
                .retrieve()
                .body(Product.class);

        // Assert: Only stock should change
        assertNotNull(updated);
        assertEquals(existing.getId(), updated.getId());
        assertEquals("Laptop", updated.getName()); // Unchanged
        assertEquals(new BigDecimal("999.99"), updated.getPrice()); // Unchanged
        assertEquals(50, updated.getStockQuantity()); // Changed
    }

    @Test
    void testDeleteProduct() {
        // Arrange: Create product to delete
        Product product = productRepository.save(
                new Product("To Delete", new BigDecimal("19.99"), "Test", 1)
        );
        Long productId = product.getId();

        // Act: DELETE request
        ResponseEntity<Void> response = restClient.delete()
                .uri("/api/products/{id}", productId)
                .retrieve()
                .toBodilessEntity();

        // Assert: Check status
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Assert: Verify deletion
        assertFalse(productRepository.existsById(productId));
    }

    @Test
    void testDeleteNonExistentProduct() {
        // Act & Assert: Attempting to delete non-existent product
        assertThrows(HttpClientErrorException.NotFound.class, () -> {
            restClient.delete()
                    .uri("/api/products/{id}", 99999L)
                    .retrieve()
                    .toBodilessEntity();
        });
    }

    // Advanced Concepts
    @Test
    void testExchangeForCustomHandling() {
        // Arrange
        Product saved = productRepository.save(
                new Product("Test Product", new BigDecimal("99.99"), "Electronics", 10)
        );

        // Act: Use exchange for custom response handling
        Product product = restClient.get()
                .uri("/api/products/{id}", saved.getId())
                .exchange((request, response) -> {
                    // Log the status
                    HttpStatusCode status = response.getStatusCode();
                    IO.println("Response Status: " + status);

                    // Read headers
                    HttpHeaders headers = response.getHeaders();
                    IO.println("Content-Type: " + headers.getContentType());

                    // Conditionally deserialize based on status
                    if (status.is2xxSuccessful()) {
                        return response.bodyTo(Product.class);
                    } else {
                        throw new RuntimeException("Failed to fetch product");
                    }
                });

        // Assert
        assertNotNull(product);
        assertEquals("Test Product", product.getName());
    }

    @Test
    void testCustomErrorHandling() {
        // Act & Assert: Custom error handling
        Exception exception = assertThrows(ProductNotFoundException.class, () -> {
            restClient.get()
                    .uri("/api/products/{id}", 99999L)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                            throw new ProductNotFoundException("Product not found");
                        }
                        throw new RuntimeException("Client error occurred");
                    })
                    .body(Product.class);
        });

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void testMultipleErrorHandlers() {
        assertThrows(ProductNotFoundException.class, () -> {
            restClient.get()
                    .uri("/api/products/{id}", 99999L)
                    .retrieve()
                    .onStatus(status -> status.value() == 404,
                            (request, response) -> {
                                throw new ProductNotFoundException("Product not found");
                            })
                    .onStatus(status -> status.value() == 403,
                            (request, response) -> {
                                throw new AccessDeniedException("Access denied");
                            })
                    .onStatus(HttpStatusCode::is5xxServerError,
                            (request, response) -> {
                                throw new ServiceException("Service unavailable");
                            })
                    .body(Product.class);
        });
    }

    @Test
    void testWithLoggingInterceptor() {
        // Create RestClient with logging
        RestClient loggedClient = RestClient.builder()
                .baseUrl("http://localhost:8080")
                .requestInterceptor(new LoggingInterceptor())
                .build();

        // Arrange
        Product saved = productRepository.save(
                new Product("Logged Product", new BigDecimal("99.99"), "Test", 5)
        );

        // Act: This request will be logged
        Product product = loggedClient.get()
                .uri("/api/products/{id}", saved.getId())
                .retrieve()
                .body(Product.class);

        // Assert
        assertNotNull(product);
        // Check console for logs
    }


}
