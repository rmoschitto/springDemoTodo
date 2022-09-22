package com.example.galvanize.springDemoToDo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodoController {

    TodoRepository repository;

    public TodoController(TodoRepository repository) {
        this.repository = repository;
    }

    //CREATE
    @PostMapping("/todo")
    public Todo createNewTodo(@RequestBody Todo todo) {
        return this.repository.save(todo);
    }

    //LIST
    @GetMapping("/MAPPING")
    p thing List<Thing> getAllThings() {
        return this.repository.findAll();
    }





}
