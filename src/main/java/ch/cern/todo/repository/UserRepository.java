package ch.cern.todo.repository;

import ch.cern.todo.model.TodoModel;
import ch.cern.todo.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
}
