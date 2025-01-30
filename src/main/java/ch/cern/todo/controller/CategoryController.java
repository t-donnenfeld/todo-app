package ch.cern.todo.controller;

import ch.cern.todo.openapi.api.CategoryApi;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryApi {
}
