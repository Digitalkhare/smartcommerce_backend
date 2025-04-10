package smartcommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import smartcommerce.model.Category;
import smartcommerce.repository.CatergoryRepository;

@Service
@RequiredArgsConstructor
public class CategoryService {
  private final CatergoryRepository catergoryRepository;
  
  public List<Category> getAllCategories(){
	  return catergoryRepository.findAll();
  }
  public Category createCategory(Category category) {
	  return catergoryRepository.save(category);
  }
} 
