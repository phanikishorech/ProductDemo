package codinpad.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import codinpad.Entity.*;;

public interface ApprovalQueueRepository extends JpaRepository<ApprovalQueue, Long> {
    List<ApprovalQueue> findAllByOrderByRequestDateAsc();
}
