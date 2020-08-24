package com.spring.customer.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomerEntityExceptionHandler extends ResponseEntityExceptionHandler {

	 @ExceptionHandler(value = CustomerNotFoundException.class)
	   public ResponseEntity<Object> customerNotFoundException(CustomerNotFoundException exception) {
	      return new ResponseEntity<>(exception.getErrorMessage(), HttpStatus.NOT_FOUND);
	   }
	 
	 @ExceptionHandler(value = CustomerAuthenticationException.class)
	   public ResponseEntity<Object> customerAuthenticationException(CustomerAuthenticationException exception) {
	      return new ResponseEntity<>(exception.getErrorMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	   }
	 
	 
	 @ExceptionHandler(Exception.class)
	    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
	        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	 
	 
	 @Override
	    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
	    		HttpHeaders headers,
	            HttpStatus status, WebRequest request) {
			List<String> errors = new ArrayList<>();
	        ex.getBindingResult().getFieldErrors().forEach(error -> 
	        errors.add(error.getDefaultMessage()));
	        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	    } 
}
