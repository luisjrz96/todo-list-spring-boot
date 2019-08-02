package com.todo.list.rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.todo.list.rest.models.dao.TaskRepository;
import com.todo.list.rest.models.entities.Task;

@Service("simple_task")
@Primary
public class TaskServiceImpl implements ITaskService{

	@Autowired
	TaskRepository taskRepository;
	
	@Override
	public Page<Task> findAll(Pageable pageable) {
		return taskRepository.findAll(pageable);
	}

	@Override
	public Task findOne(Long id) {
		return taskRepository.findById(id).orElse(null);
	}

	@Override
	public void save(Task task) {
		taskRepository.save(task);
	}

	@Override
	public void delete(Task task) {
		taskRepository.delete(task);
	}

}
