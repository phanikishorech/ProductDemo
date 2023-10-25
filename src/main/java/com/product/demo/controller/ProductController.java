package codinpad.controller;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import codinpad.service.ProductService;
import codinpad.Entity.*;


@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    
    @GetMapping
    public List<Product> listActiveProducts() {
        return productService.listActiveProducts();
    }
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        if (product.getPrice().compareTo(new BigDecimal("10000")) > 0) {
            return ResponseEntity.badRequest().body(null); // Price exceeds $10,000, return a bad request.
        } -

        Product createdProduct = productService.createProduct(product);

        if (createdProduct.isStatus()) {
            return ResponseEntity.ok(createdProduct); // Product is active.
        } else {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(createdProduct); // Product is pending approval.
        }
    }

    PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long productId,
            @RequestBody Product updatedProduct) {
        Product updatedProduct = productService.updateProduct(productId, updatedProduct);
        return ResponseEntity.ok(updatedProduct);
    }

    // Exception handler for ProductNotFoundException
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProductAndPushToApprovalQueue(@PathVariable Long productId) {
        productService.deleteProductAndPushToApprovalQueue(productId);
        return ResponseEntity.noContent().build();
    }
}
