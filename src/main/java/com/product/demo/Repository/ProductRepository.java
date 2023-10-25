package codinpad.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import codinpad.Entity.*;;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIsActiveOrderByDateCreatedDesc(boolean isActive);
}