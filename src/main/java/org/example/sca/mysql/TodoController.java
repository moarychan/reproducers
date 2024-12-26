// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package org.example.sca.mysql;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/todo")
public class TodoController {

    private final TodoRepository todoRepository;

    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Todo createTodo(@RequestBody TodoDTO todoDTO) {
        Todo todo = new Todo(todoDTO.getDescription(), todoDTO.getDetails(), todoDTO.isDone());
        return todoRepository.save(todo);
    }

    @GetMapping
    public Iterable<Todo> getTodos() {
        return todoRepository.findAll();
    }
}
