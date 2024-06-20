package com.dws.challenge.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DuplicateAccountIdException.class)
	public ResponseEntity<Object> handleDuplicateAccountIdException(DuplicateAccountIdException ex) {
		return ResponseEntity.badRequest().body(ex.getMessage());
	}

	@ExceptionHandler(InsufficientBalanceException.class)
	public ResponseEntity<Object> handleInsufficientBalanceException(InsufficientBalanceException ex) {
		return ResponseEntity.badRequest().body(ex.getMessage());
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
		return ResponseEntity.internalServerError().body(ex.getMessage());
	}

	// Add more exception handlers as needed for different scenarios
}
