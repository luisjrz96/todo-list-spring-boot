package com.todo.list.rest.exception;


import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.print.URIException;
import javax.validation.ConstraintViolationException;


@ControllerAdvice
public class  TaskExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Object> handleTaskNotFoundException(TaskNotFoundException ex, WebRequest request){
    	List<String> errors = Arrays.asList("Unable to find requested task");
    	TaskErrorResponse errorResponse = new TaskErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), errors);
    	return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }
    
   

   @ExceptionHandler
    public ResponseEntity<Object> handleTaskNotValidFields(ConstraintViolationException ex, WebRequest request) {
	   List<String> errors = ex.getConstraintViolations()
			   .stream()
			   .map(e -> String.format("%s:%s", e.getPropertyPath().toString(), e.getMessage()))
			   .collect(Collectors.toList());
	   
    	TaskErrorResponse errorResponse = new TaskErrorResponse(HttpStatus.BAD_REQUEST, "Invalid fields while trying to create the new task" , errors); 
    	return new ResponseEntity<>(errorResponse, errorResponse.getStatus());

    }
   
   
  @ExceptionHandler
  public ResponseEntity<Object> handleTaskTypeMismatchURIException(MethodArgumentTypeMismatchException ex, WebRequest request){
	 	  
	  List<String> errors = new ArrayList<>();
			  errors.add(String.format("Invalid argument for path variable \"%s\" - given value \"%s\" - expected type \"%s\"",
					  ex.getName() ,ex.getValue().toString(), ex.getParameter().getNestedGenericParameterType().getTypeName().toString()));
			  
	  TaskErrorResponse errorResponse = new TaskErrorResponse(HttpStatus.BAD_REQUEST, "Error: ".concat(ex.ERROR_CODE.toString()) , errors);
	  return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
  }
 
    
}
