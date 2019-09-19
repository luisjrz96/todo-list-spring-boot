package com.todo.list.rest.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.todo.list.rest.models.entities.Task;

public interface ITaskService {

	public Page<Task> findAll(Pageable pageable);
	
	public Task findOne(Long id);
	
	public Task save(Task task);
	
	public void delete(Task task);
}
