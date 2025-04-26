package smartcommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import smartcommerce.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findByCategoryId(Long categoryId);
	 List<Product> findByFeaturedTrue();
	 List<Product> findByCategory_NameIgnoreCase(String name);
	 List<Product> findByCategory_NameAndSubCategory_NameIn(String categoryName, List<String> subCategoryNames);

}
