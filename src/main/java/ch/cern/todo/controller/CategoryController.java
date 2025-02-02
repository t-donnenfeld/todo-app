package ch.cern.todo.controller;

import ch.cern.todo.openapi.api.CategoryApi;
import ch.cern.todo.openapi.model.AddCategoryRequest;
import ch.cern.todo.openapi.model.Category;
import ch.cern.todo.openapi.model.DeletedEntryResponse;
import ch.cern.todo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_USER')")
public class CategoryController implements CategoryApi {

    private final CategoryService categoryService;

    @Override
    public ResponseEntity<List<Category>> getAllCategory() {
        List<Category> categories = categoryService.findAllCategories();
        return ResponseEntity.ok(categories);
    }

    @Override
    public ResponseEntity<Category> addCategory(AddCategoryRequest addCategoryRequest) {
        Category createdCategory = categoryService.addCategory(addCategoryRequest);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Category> updateCategory(Long categoryId, AddCategoryRequest addCategoryRequest) {
        Category updatedCategory = categoryService.updateCategory(categoryId, addCategoryRequest);
        return new ResponseEntity<>(updatedCategory, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<DeletedEntryResponse> deleteCategory(Long categoryId) {
        categoryService.deleteCategory(categoryId);

        DeletedEntryResponse deletedEntryResponse = new DeletedEntryResponse();
        deletedEntryResponse.setMessage("Category with id " + categoryId + " deleted");

        return new ResponseEntity<>(deletedEntryResponse, HttpStatus.OK);
    }

}
