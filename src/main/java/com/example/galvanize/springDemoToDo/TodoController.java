package com.example.galvanize.springDemoToDo;

import org.hibernate.annotations.Filter;
import org.springframework.context.annotation.ComponentScan;
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
    @GetMapping("/todo")
    public List<Todo> getAllTodos() {
        return this.repository.findAll();
    }

    //READ
    @GetMapping("/todo/{id}")
    public Todo getOneTodoById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException(String.format("Todo with id %d is not present", id)));
    }

    //DELETE
    @DeleteMapping("/todo/{id}")
    public ResponseEntity<Object> deleteMapping(@PathVariable Long id) {
        this.repository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //UPDATE

    @PatchMapping("/todo/{id}")
    public Todo updateTodo(@PathVariable Long id, @RequestBody Map<String, String> body) {
        // Map is creating a key value pair
        // First step find the old record
        Todo oldTodo = repository.findById(id).orElseThrow(() -> new NoSuchElementException(String.format("Todo with id %d is not present", id)));

        // Figure out what to edit
        body.forEach(
                (key, value) -> {
                    // Edit those fields
                    if (key.equals("description")) {
                        oldTodo.setDescription(value);
                    } else if (key.equals("dueDate")) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        //convert String to LocalDate
                        LocalDate localDate = LocalDate.parse(value, formatter);
                        oldTodo.setDueDate(localDate);
                    } else if (key.equals("priority")) {
                        oldTodo.setPriority(value);
                    }
                }
        );
        //save the record
        return this.repository.save(oldTodo);
    }

    //Filter Priority
    @RequestMapping("/todo/filter")
    public List<Todo> filterTodos(@RequestParam String filter) {
        return this.repository.findAllByPriority(filter);
    }

}