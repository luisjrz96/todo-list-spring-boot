package com.todo.list.rest.exception;



public class TaskNotFoundException extends RuntimeException{

    public TaskNotFoundException(Long id){
        super("Task id not found : "+ id);
    }
}
