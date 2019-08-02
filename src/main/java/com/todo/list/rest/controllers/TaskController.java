package com.todo.list.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.todo.list.rest.models.entities.Task;
import com.todo.list.rest.services.ITaskService;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
@RequestMapping("/api/v1/tasks")
public class TaskController {
	 
	private final int DEF_PAGE = 0;
	private final int PAGE_SIZE = 5;
	private final String SORTED_BY = "id";
	private final int ORDER_BY_DESC = 0;
	
	@Autowired
	private ITaskService taskService;
	
	@GetMapping
	public Page<Task> showAll(
			@RequestParam(name = "page", defaultValue = "0") Integer page, 
			@RequestParam(name = "page_size", required = false, defaultValue = "5") Integer pageSize,
			@RequestParam(name = "sorted_by", required = false, defaultValue = "id") String sortedBy,
			@RequestParam(name= "is_order_desc", required = false, defaultValue = "0") Integer orderBy){
		
		Pageable pageable;
		try {
			int pageX = (page == null)? DEF_PAGE: page;
			int pageSizeX = (pageSize != null)? pageSize : PAGE_SIZE;
			String sortedByX = (sortedBy != null || sortedBy != "day" || sortedBy != "title" )? sortedBy: SORTED_BY;
			int orderByX = (orderBy != null && (orderBy > -1 && orderBy < 2))? orderBy : ORDER_BY_DESC;
		
			if(orderByX == 1) {
				pageable = PageRequest.of(pageX, pageSizeX, Sort.by(sortedByX).descending());
			}else {
				pageable = PageRequest.of(pageX, pageSizeX, Sort.by(sortedByX).ascending());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			pageable = PageRequest.of(DEF_PAGE, PAGE_SIZE, Sort.by(SORTED_BY));
		}
		return taskService.findAll(pageable);
	}
	
	@GetMapping("/{id}")
	public Task showItem(@PathVariable(name = "id") Long id) {
		return taskService.findOne(id);
	}
	
	
	@PostMapping
	public void create(@RequestBody Task task) {
		taskService.save(task);
	}
	
	@PutMapping("/{id}")
	public void update(@RequestBody Task task, @PathVariable(name="id") Long id) {
		Task taskToUpdate = taskService.findOne(id);
		if(taskToUpdate != null) {
			taskToUpdate.setTitle(task.getTitle());
			taskToUpdate.setDescription(task.getDescription());
			taskToUpdate.setHour(task.getHour());
			taskToUpdate.setDay(task.getDay());
			taskToUpdate.setDone(task.isDone());
			taskService.save(taskToUpdate);
		}
	}
	
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable(name = "id") Long id) {
		Task taskToDelete = taskService.findOne(id);
		taskService.delete(taskToDelete);
	}
}
