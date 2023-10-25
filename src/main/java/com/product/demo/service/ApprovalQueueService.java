package codinpad.service;

@Service
public class ApprovalQueueService {
    @Autowired
    private ApprovalQueueRepository approvalQueueRepository;

    @Autowired
    private ProductRepository productRepository;
    
    public List<ApprovalQueue> getProductsInApprovalQueue() {
        return approvalQueueRepository.findAllByOrderByRequestDateAsc();
    }
    
    public void approveProduct(Long approvalId) {
        Optional<ApprovalQueue> approvalQueueOptional = approvalQueueRepository.findById(approvalId);

        if (approvalQueueOptional.isPresent()) {
            ApprovalQueue approvalQueue = approvalQueueOptional.get();
            Long productId = approvalQueue.getProductId();
            Optional<Product> productOptional = productRepository.findById(productId);

            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                product.setStatus(true); // Update the product status
                productRepository.save(product); // Save the updated product
                approvalQueueRepository.delete(approvalQueue); // Remove from approval queue
            } else {
                throw new NotFoundException("Product not found with ID: " + productId);
            }
        } else {
            throw new NotFoundException("Approval entry not found with ID: " + approvalId);
        }
    }
    
    public void rejectProduct(Long approvalId) {
        Optional<ApprovalQueue> approvalQueueOptional = approvalQueueRepository.findById(approvalId);

        if (approvalQueueOptional.isPresent()) {
            ApprovalQueue approvalQueue = approvalQueueOptional.get();
            approvalQueueRepository.delete(approvalQueue); // Remove from approval queue
        } else {
            throw new NotFoundException("Approval entry not found with ID: " + approvalId);
        }
    }
}