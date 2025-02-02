package ch.cern.todo.repository;

import ch.cern.todo.model.TodoModel;
import ch.cern.todo.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoModel, Long> {

    List<TodoModel> findByUserId(UserModel user);

    @Query("SELECT todo FROM TodoModel todo WHERE (:id is null or todo.id=:id)" +
            "AND (:nameContains is null or todo.name LIKE %:nameContains%)" +
            "AND (:descriptionContains is null or todo.description LIKE %:descriptionContains%)" +
            "AND (:deadlineAfter is null or todo.deadline > :deadlineAfter)" +
            "AND (:deadlineBefore is null or todo.deadline < :deadlineBefore)" +
            "AND (:categoryIs is null or todo.categoryId.name = :categoryIs)" +
            "AND (:usernameIs is null or todo.userId.username = :usernameIs)")
    List<TodoModel> search(@Param("id") Long id,
                           @Param("nameContains") String nameContains,
                           @Param("descriptionContains") String descriptionContains,
                           @Param("deadlineAfter") LocalDateTime deadlineAfter,
                           @Param("deadlineBefore") LocalDateTime deadlineBefore,
                           @Param("categoryIs") String categoryIs,
                           @Param("usernameIs") String usernameIs);


}
