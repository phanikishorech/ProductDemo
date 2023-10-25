package codinpad.service;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import codinpad.repository.ProductRepository;
import codinpad.Entity.*;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> listActiveProducts() {
        return productRepository.findByIsActiveOrderByDateCreatedDesc(true);
    }

    public Product createProduct(Product product) {
        if (product.getPrice().compareTo(new BigDecimal("5000")) > 0) {
            // Price exceeds $5,000, push to approval queue or perform any desired action.
            // For this example, we'll set the status to 'pending' for approval.
            product.setStatus(false);
        } else {
            product.setStatus(true);
        }
        return productRepository.save(product);
    }

    public Product updateProduct(Long productId, Product updatedProduct) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));

        BigDecimal previousPrice = existingProduct.getPrice();
        BigDecimal newPrice = updatedProduct.getPrice();

        // Check if the price increase is more than 50% of the previous price.
        if (newPrice.subtract(previousPrice).compareTo(previousPrice.divide(new BigDecimal(2))) > 0) {
            // Price increase exceeds 50%, push to approval queue or take other action.
            // For this example, we'll set the status to 'pending' for approval.
            updatedProduct.setStatus(false);
        } else {
            updatedProduct.setStatus(existingProduct.isStatus());
        }

        updatedProduct.setId(productId); // Ensure the ID remains the same.

        return productRepository.save(updatedProduct);
    }

    public void deleteProductAndPushToApprovalQueue(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            // Create an approval queue entry
            ApprovalQueue approvalQueueEntry = new ApprovalQueue();
            approvalQueueEntry.setProductId(productId);
            approvalQueueEntry.setRequestDate(LocalDateTime.now());

            // Save the product to the approval queue
            approvalQueueRepository.save(approvalQueueEntry);

            // Delete the product
            productRepository.delete(product);
        } else {
            throw new NotFoundException("Product not found with ID: " + productId);
        }
    }
}