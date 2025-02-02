package ch.cern.todo.service;

import ch.cern.todo.error.ForbiddenException;
import ch.cern.todo.error.ResourceNotFound;
import ch.cern.todo.mapper.TodoMapper;
import ch.cern.todo.model.RoleEnum;
import ch.cern.todo.model.TodoModel;
import ch.cern.todo.model.UserModel;
import ch.cern.todo.openapi.model.*;
import ch.cern.todo.repository.CategoryRepository;
import ch.cern.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;
    private final UserService userService;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    public Todo findTodoById(Long id) {
        validateUserRights(id);
        return todoRepository.findById(id).map(todoMapper::map).orElseThrow(() -> new ResourceNotFound("Todo with id " + id + " not found"));
    }

    public List<Todo> findAllTodos() {
        return todoRepository.findAll().stream().map(todoMapper::map).toList();
    }

    public List<Todo> findAllTodosOfAuthenticatedUser() {
        UserModel loggedUser = userService.getAuthentifiedUser();
        return todoRepository.findByUserId(loggedUser).stream().map(todoMapper::map).toList();
    }

    public List<Todo> searchTodos(SearchTodosRequest searchTodosRequest) {
        return todoRepository.search(searchTodosRequest.getId(),
                        searchTodosRequest.getNameContains(),
                        searchTodosRequest.getDescriptionContains(),
                        searchTodosRequest.getDeadlineAfter(),
                        searchTodosRequest.getDeadlineBefore(),
                        searchTodosRequest.getCategoryIs(),
                        searchTodosRequest.getOwner())
                .stream().map(todoMapper::map).toList();
    }

    public List<Todo> searchTodosOfAuthenticatedUser(SearchMyTodosRequest searchMyTodosRequest) {
        UserModel loggedUser = userService.getAuthentifiedUser();
        return todoRepository.search(searchMyTodosRequest.getId(),
                        searchMyTodosRequest.getNameContains(),
                        searchMyTodosRequest.getDescriptionContains(),
                        searchMyTodosRequest.getDeadlineAfter(),
                        searchMyTodosRequest.getDeadlineBefore(),
                        searchMyTodosRequest.getCategoryIs(),
                        loggedUser.getUsername()
                )
                .stream().map(todoMapper::map).toList();
    }

    public Todo addTodo(AddTodoRequest addTodoRequest) {
        TodoModel toSave = todoMapper.map(addTodoRequest, null);
        toSave.setUserId(userService.getAuthentifiedUser());
        if (addTodoRequest.getCategory() != null) {
            Category retrieved = categoryService.resolveOrCreateCategory(addTodoRequest.getCategory().getName());
            toSave.setCategoryId(categoryRepository.findByName(retrieved.getName()));
        }
        return todoMapper.map(todoRepository.save(toSave));
    }


    public Todo updateTodo(Long id, AddTodoRequest addTodoRequest) {
        validateUserRights(id);

        TodoModel toSave = todoMapper.map(addTodoRequest, id);

        Optional<TodoModel> todoModelOpt = todoRepository.findById(id);
        todoModelOpt.ifPresentOrElse(
                todoModel -> toSave.setUserId(todoModel.getUserId()),
                () -> {
                    throw new ResourceNotFound("Could not find todo with id " + id);
                });

        if (addTodoRequest.getCategory() != null) {
            Category retrieved = categoryService.resolveOrCreateCategory(addTodoRequest.getCategory().getName());
            toSave.setCategoryId(categoryRepository.findByName(retrieved.getName()));
        }
        return todoMapper.map(todoRepository.save(toSave));
    }

    public void deleteTodo(Long id) {
        validateUserRights(id);
        todoRepository.deleteById(id);
    }

    private void validateUserRights(Long id) {
        if (!contextUserHasRightsOnTodo(id)) {
            throw new ForbiddenException("Connected user does not have the right on todo with id " + id);
        }
    }

    private boolean contextUserHasRightsOnTodo(Long todoId) {
        UserModel loggedUser = userService.getAuthentifiedUser();
        if (loggedUser.getRole() == RoleEnum.ADMIN) {
            return true;
        }
        Optional<TodoModel> todoOpt = todoRepository.findById(todoId);
        if (todoOpt.isEmpty()) {
            throw new ResourceNotFound("Could not find todo with id " + todoId);
        }
        return Objects.equals(todoOpt.get().getUserId().getId(), loggedUser.getId());
    }

}
