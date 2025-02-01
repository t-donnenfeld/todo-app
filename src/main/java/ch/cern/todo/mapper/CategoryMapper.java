package ch.cern.todo.mapper;

import ch.cern.todo.model.CategoryModel;
import ch.cern.todo.openapi.model.AddCategoryRequest;
import ch.cern.todo.openapi.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "name", source = "category.name")
    @Mapping(target = "description", source = "category.description")
    @Mapping(target = "id", source = "id")
    CategoryModel map(AddCategoryRequest category, Long id);

    CategoryModel map(AddCategoryRequest category);

    Category map(CategoryModel categoryModel);

}
