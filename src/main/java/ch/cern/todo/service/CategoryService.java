package ch.cern.todo.service;

import ch.cern.todo.error.ResourceNotFound;
import ch.cern.todo.error.ResourceAlreadyExistsException;
import ch.cern.todo.mapper.CategoryMapper;
import ch.cern.todo.openapi.model.AddCategoryRequest;
import ch.cern.todo.openapi.model.Category;
import ch.cern.todo.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private Optional<Category> findCategoryById(Long id) {
        return categoryRepository.findById(id).map(categoryMapper::map);
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll().stream().map(categoryMapper::map).toList();
    }

    public Category findByName(String name) {
        return categoryMapper.map(categoryRepository.findByName(name));
    }

    public Category addCategory(AddCategoryRequest addCategoryRequest) {
        checkCategoryExists(addCategoryRequest.getName());
        return categoryMapper.map(categoryRepository.save(categoryMapper.map(addCategoryRequest)));
    }

    public Category addCategoryWithName(String name) {
        checkCategoryExists(name);
        return categoryMapper.map(categoryRepository.save(categoryMapper.map(name)));
    }

    private void checkCategoryExists(String name) {
        if (categoryRepository.findByName(name) != null) {
            throw new ResourceAlreadyExistsException("Category " + name + " already exists");
        }
    }

    public Category resolveOrCreateCategory(String categoryName) {
        Category category = findByName(categoryName);
        if (category == null) {
            category = addCategoryWithName(categoryName);
        }
        return category;
    }

    public Category updateCategory(Long id, AddCategoryRequest addCategoryRequest) {
        findCategoryById(id).orElseThrow(() -> new ResourceNotFound("Category with id " + id + " not found"));
        return categoryMapper.map(categoryRepository.save(categoryMapper.map(addCategoryRequest, id)));
    }

    public void deleteCategory(Long id) {
        findCategoryById(id).orElseThrow(() -> new ResourceNotFound("Category with id " + id + " not found"));
        categoryRepository.deleteById(id);
    }

}
