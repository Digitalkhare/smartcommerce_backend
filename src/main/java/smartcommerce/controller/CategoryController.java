package smartcommerce.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import smartcommerce.model.Category;
import smartcommerce.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public Category create(@RequestBody Category category) {
    	return categoryService.createCategory(category);
    }
    @GetMapping
    public List<Category> getAllCategories() {
    	 log.info("Fetching all categories...");
        return categoryService.getAllCategories();
    }
}
