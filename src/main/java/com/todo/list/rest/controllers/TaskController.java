package com.todo.list.rest.controllers;

import com.todo.list.rest.exception.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import com.todo.list.rest.models.entities.Task;
import com.todo.list.rest.services.ITaskService;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/tasks")
public class TaskController {

	@Autowired
	private ITaskService taskService;

	@GetMapping
	public ResponseEntity<Map<String, Object>> showAll(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "page_size", required = false, defaultValue = "5") Integer pageSize,
			@RequestParam(name = "sorted_by", required = false, defaultValue = "id") String sortedBy,
			@RequestParam(name = "order_desc", required = false, defaultValue = "0") Integer orderBy) {

		Map<String, Object> responseBody = new HashMap<>();

		Pageable pageable = null;
		Page<Task> pageData = null;
		int requestPageSize = pageSize;
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
				responseBody.put("page", pageData);
				responseBody.put("status", HttpStatus.OK.value());
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(responseBody);
			} else {
				responseBody.put("errors", "Page not found");
				responseBody.put("status", HttpStatus.NOT_FOUND.value());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
			}
		} catch (Exception err) {
			responseBody.put("errors", "Internal Server Error");
			responseBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> showItem(@PathVariable(name = "id") Long id) {
		Map<String, Object> responseBody = new HashMap<>();
		try {
			Task taskToShow = taskService.findOne(id);
			if (taskToShow != null) {
				responseBody.put("task", taskToShow);
				responseBody.put("status", HttpStatus.OK.value());
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(responseBody);
			} else {
				throw new TaskNotFoundException(id);
			}
		} catch (DataAccessException err) {
			responseBody.put("errors", "Internal Server Error");
			responseBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
		}
	}

	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody Task task, BindingResult validationResult) {
		Map<String, Object> responseBody = new HashMap<>();
		try {
			Task newTask = taskService.save(task);
			responseBody.put("task", newTask);
			responseBody.put("status", HttpStatus.CREATED.value());
			return ResponseEntity
					.created(URI.create(String.format("http://localhost/api/v1/tasks/%s", newTask.getId())))
					.contentType(MediaType.APPLICATION_JSON_UTF8).body(responseBody);

		} catch (DataAccessException err) {
			responseBody.put("errors", "Internal server error");
			responseBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
		}

	}

	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@Valid @RequestBody Task task, BindingResult taskInfo,
			@PathVariable(name = "id") Long id) {
		Map<String, Object> responseBody = new HashMap<>();
		try {
			Task taskToUpdate = taskService.findOne(id);
			if (taskToUpdate != null) {
					taskToUpdate.setTitle(task.getTitle());
					taskToUpdate.setDescription(task.getDescription());
					taskToUpdate.setTimeTodo(task.getTimeTodo());
					taskToUpdate.setDateTodo(task.getDateTodo());
					taskToUpdate.setDone(task.isDone());
					taskService.save(taskToUpdate);

					responseBody.put("task", taskToUpdate);
					responseBody.put("status", HttpStatus.OK.value());
					return ResponseEntity.created(URI.create(String.format("http://localhost/api/v1/tasks/%s", id)))
							.contentType(MediaType.APPLICATION_JSON_UTF8).body(responseBody);
				
			} else {
				throw new TaskNotFoundException(id); 
			}
		} catch (DataAccessException err) {
			responseBody.put("Errors", "Internal server error");
			responseBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, Object>> delete(@PathVariable(name = "id") Long id) {
		Map<String, Object> responseBody = new HashMap<>();
		try {
			Task taskToDelete = taskService.findOne(id);
			if (taskToDelete != null) {
				taskService.delete(taskToDelete);
				responseBody.put("task", taskToDelete);
				responseBody.put("status", HttpStatus.OK.value());
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(responseBody);
			} else {
				throw new TaskNotFoundException(id);
			}
		} catch (DataAccessException err) {
			responseBody.put("errors", "Internal server error");
			responseBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
		}
	}

}
