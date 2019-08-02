package com.todo.list.rest.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.todo.list.rest.models.entities.Task;

@Repository
public interface TaskRepository extends PagingAndSortingRepository<Task, Long>{

}
