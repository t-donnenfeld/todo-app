package ch.cern.todo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "CATEGORIES")
@Getter
@Setter
public class CategoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "category")
    private List<TodoModel> todos;

    @PreRemove
    private void preRemove() {
        for (TodoModel t : todos) {
            t.setCategory(null);
        }
    }

    @Column(unique = true, name = "category_name")
    private String name;

    @Column(name = "category_description")
    private String description;

}
