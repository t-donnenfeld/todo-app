package ch.cern.todo.repository;

import ch.cern.todo.model.CategoryModel;
import ch.cern.todo.model.TodoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryModel, Long> {
}
