package com.todo.list.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.todo.list.rest.models.entities.Task;
import com.todo.list.rest.services.ITaskService;
import javax.validation.Valid;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/tasks")
public class TaskController {

    @Autowired
    private ITaskService taskService;

    @GetMapping
    public ResponseEntity<Page<Task>> showAll(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "page_size", required = false, defaultValue = "5") Integer pageSize,
            @RequestParam(name = "sorted_by", required = false, defaultValue = "id") String sortedBy,
            @RequestParam(name = "order_desc", required = false, defaultValue = "0") Integer orderBy) {

        Pageable pageable = null;
        Page<Task> pageData = null;
        int requestPageSize =  pageSize;
        String requestSortedBy = sortedBy;
        int requestOrderBy = orderBy;

        try {
            if (requestOrderBy == 1) {
                pageable = PageRequest.of(page, requestPageSize, Sort.by(requestSortedBy).descending());
            } else {
                pageable = PageRequest.of(page, requestPageSize, Sort.by(requestSortedBy).ascending());
            }
            pageData = taskService.findAll(pageable);
            if (page < pageData.getTotalPages()) {
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(pageData);
               } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception err) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> showItem(@PathVariable(name = "id") Long id) {
        try {
            Task taskToShow = taskService.findOne(id);
            if (taskToShow != null) {
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(taskToShow);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (DataAccessException err) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping
    public ResponseEntity<Task> create(@Valid @RequestBody Task task, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            try {
                Task newTask = taskService.save(task);
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(newTask);
            } catch (DataAccessException err) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@Valid @RequestBody Task task, BindingResult taskInfo, @PathVariable(name = "id") Long id) {
        try {
            Task taskToUpdate = taskService.findOne(id);
            if (taskToUpdate != null) {
                if (!taskInfo.hasErrors()) {
                    taskToUpdate.setTitle(task.getTitle());
                    taskToUpdate.setDescription(task.getDescription());
                    taskToUpdate.setTimeTodo(task.getTimeTodo());
                    taskToUpdate.setDateTodo(task.getDateTodo());
                    taskToUpdate.setDone(task.isDone());
                    taskService.save(taskToUpdate);
                    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(taskToUpdate);
                } else {
                    return ResponseEntity.badRequest().build();
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (DataAccessException err) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Task> delete(@PathVariable(name = "id") Long id) {
        try {
            Task taskToDelete = taskService.findOne(id);
            if (taskToDelete != null) {
                taskService.delete(taskToDelete);
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(taskToDelete);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (DataAccessException err) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
