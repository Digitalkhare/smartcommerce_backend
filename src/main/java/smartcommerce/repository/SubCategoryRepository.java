package smartcommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import smartcommerce.model.Category;
import smartcommerce.model.SubCategory;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    List<SubCategory> findByCategory(Category category);
}
