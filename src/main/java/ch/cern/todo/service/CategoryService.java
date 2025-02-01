package ch.cern.todo.service;

import ch.cern.todo.error.ResourceNotFound;
import ch.cern.todo.mapper.CategoryMapper;
import ch.cern.todo.openapi.model.AddCategoryRequest;
import ch.cern.todo.openapi.model.Category;
import ch.cern.todo.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id).map(categoryMapper::map).orElseThrow(() -> new ResourceNotFound("Category with id " + id + " not found"));
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll().stream().map(categoryMapper::map).toList();
    }

    public Category addCategory(AddCategoryRequest addCategoryRequest) {
        return categoryMapper.map(categoryRepository.save(categoryMapper.map(addCategoryRequest)));
    }

    public Category updateCategory(Long id, AddCategoryRequest addCategoryRequest) {
        findCategoryById(id);
        return categoryMapper.map(categoryRepository.save(categoryMapper.map(addCategoryRequest, id)));
    }

    public void deleteCategory(Long id) {
        findCategoryById(id);
        categoryRepository.deleteById(id);
    }

}
