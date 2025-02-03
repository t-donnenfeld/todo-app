package ch.cern.todo.mapper;

import ch.cern.todo.model.TodoModel;
import ch.cern.todo.openapi.model.AddTodoRequest;
import ch.cern.todo.openapi.model.Todo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TodoMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "category.name", ignore = true)
    @Mapping(target = "category.id", ignore = true)
    @Mapping(target = "category.description", ignore = true)
    @Mapping(target = "id", source = "idOfTodoToUpdate")
    @Mapping(target = "name", source = "addTodoRequest.name")
    @Mapping(target = "description", source = "addTodoRequest.description")
    @Mapping(target = "deadline", source = "addTodoRequest.deadline")
    TodoModel map(AddTodoRequest addTodoRequest, Long idOfTodoToUpdate);


    @Mapping(target = "owner", source = "user.username")
    @Mapping(target = "category.name", source = "category.name")
    @Mapping(target = "category.id", source = "category.id")
    Todo map(TodoModel todoModel);
}
