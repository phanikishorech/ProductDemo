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
import codinpad.service.ApprovalQueueService;
import codinpad.Entity.*;


@RestController
@RequestMapping("/api/products/approval-queue")
public class ProductController {

    @Autowired
    private ApprovalQueueService approvalQueueService;
        
    @GetMapping
    public ResponseEntity<List<ApprovalQueue>> getProductsInApprovalQueue() {
        List<ApprovalQueue> approvalQueue = approvalQueueService.getProductsInApprovalQueue();
        return new ResponseEntity<>(approvalQueue, HttpStatus.OK);
    }
    
    @PutMapping("/{approvalId}/approve")
    public ResponseEntity<Void> approveProduct(@PathVariable Long approvalId) {
        approvalQueueService.approveProduct(approvalId);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{approvalId}/reject")
    public ResponseEntity<Void> rejectProduct(@PathVariable Long approvalId) {
        approvalQueueService.rejectProduct(approvalId);
        return ResponseEntity.noContent().build();
    }
}
