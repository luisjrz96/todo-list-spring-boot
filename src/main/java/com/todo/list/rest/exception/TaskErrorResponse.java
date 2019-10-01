package com.todo.list.rest.exception;

import org.springframework.http.HttpStatus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TaskErrorResponse {
    private HttpStatus status;
    private String message;
    private List<String> errors;
    private String timestamp;
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
    
    public TaskErrorResponse(HttpStatus status, String message, List<String> errors){
        
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timestamp = dateFormat.format(new Date());
    }


    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
    
    public String getTimestamp() {
    	return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
    	this.timestamp = timestamp;
    }
}
