package ch.cern.todo.controller;

import ch.cern.todo.error.NotImplementedException;
import ch.cern.todo.openapi.api.CategoryApi;
import ch.cern.todo.openapi.model.AddCategoryRequest;
import ch.cern.todo.openapi.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryApi {

    @Override
    public ResponseEntity<List<Category>> getAllCategory() {
        throw new NotImplementedException("getAllCategory is not yet implemented");
    }

    @Override
    public ResponseEntity<Category> addCategory(AddCategoryRequest addCategoryRequest) {
        return CategoryApi.super.addCategory(addCategoryRequest);
    }

    @Override
    public ResponseEntity<Category> updateCategory(Long categoryId, AddCategoryRequest addCategoryRequest) {
        return CategoryApi.super.updateCategory(categoryId, addCategoryRequest);
    }

    @Override
    public ResponseEntity<Void> deleteCategory(Long categoryId) {
        throw new NotImplementedException("deleteCategory is not yet implemented");
    }

}
