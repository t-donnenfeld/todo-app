package ch.cern.todo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "todos")
@Getter
@Setter
public class TodoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "todo_name")
    private String name;

    @Column(name = "todo_description")
    private String description;

    @Column(name = "todo_deadline", columnDefinition = "TIMESTAMP")
    private LocalDateTime deadline;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryModel categoryId;


    public TodoModel() {
    }

}
