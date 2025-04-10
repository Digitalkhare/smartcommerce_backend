package smartcommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import smartcommerce.model.Category;

public interface CatergoryRepository extends JpaRepository<Category, Long> {

}
