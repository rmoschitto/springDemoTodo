package com.example.galvanize.springDemoToDo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    TodoRepository repository;

    Todo todo1;
    Todo todo2;

    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setup() {
        todo1 = new Todo();
        todo2 = new Todo();
        todo1.setDescription("Take out Trash");
        todo1.setPriority("low");
        todo1.setDueDate(LocalDate.of(2022, 9, 23));
    }

    //POST
    @Test
    @Transactional
    @Rollback
    public void createANewToDo() throws Exception {
        String json = mapper.writeValueAsString(todo1);
        MockHttpServletRequestBuilder request = post("/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        this.mvc.perform(request)
                .andExpect(status().isOk());
        assertEquals(1, this.repository.count());
        this.mvc.perform(get("/todo"))
                .andExpect(jsonPath("$[0].description").value("Take out Trash"))
                .andExpect(jsonPath("$[0].priority").value("low"))
                .andExpect(jsonPath("$[0].dueDate").value("2022-09-23"));

    }

    //LIST
    @Test
    @Transactional
    @Rollback
    public void createListOfTodos() throws Exception {
        MockHttpServletRequestBuilder request = get("/todo");
        this.repository.save(todo1);
        this.repository.save(todo2);
        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].description").value("Take out Trash"))
                .andExpect(jsonPath("$[0].priority").value("low"))
                .andExpect(jsonPath("$[0].dueDate").value("2022-09-23"));
    }

    //READ
    @Test
    @Transactional
    @Rollback
    public void getOneTodo() throws Exception {
        this.repository.save(todo1);
        this.repository.save(todo2);

        String url = String.format("/todo/%d", todo1.getId());
        MockHttpServletRequestBuilder request = get(url);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Take out Trash"))
                .andExpect(jsonPath("$.priority").value("low"))
                .andExpect(jsonPath("$.dueDate").value("2022-09-23"));

    }

    //DELETE
    @Test
    @Transactional
    @Rollback
    public void deleteATodo() throws Exception {
        this.repository.save(todo1);
        this.repository.save(todo2);

        String url = String.format("/todo/%d", todo1.getId());
        MockHttpServletRequestBuilder request = delete(url);
        this.mvc.perform(request)
                .andExpect(status().isNoContent());
        assertEquals(1, this.repository.count());
    }

    //UPDATE
    @Test
    @Transactional
    @Rollback
    public void updateATodo() throws Exception {
        this.repository.save(todo1);

        //make JSON for patch request
        String json = "{\"description\":\"DO NOT TAKE OUT TRASH\"}";

        //Make request
        MockHttpServletRequestBuilder request = patch(String.format("/todo/%d", todo1.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //Send the request
        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("DO NOT TAKE OUT TRASH"));

        this.mvc.perform(get(String.format("/todo/%d", todo1.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("DO NOT TAKE OUT TRASH"));
    }
//
//    @Test
//    @Transactional
//    @Rollback
//    public void
}
