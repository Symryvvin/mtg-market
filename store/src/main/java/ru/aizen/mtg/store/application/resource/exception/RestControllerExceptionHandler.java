package ru.aizen.mtg.store.application.resource.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.aizen.mtg.store.application.resource.dto.response.Error;
import ru.aizen.mtg.store.domain.single.SingleNotFoundException;
import ru.aizen.mtg.store.domain.store.StoreNotFountException;

@ControllerAdvice
class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({StoreNotFountException.class, SingleNotFoundException.class})
	public ResponseEntity<Error> handleNotFound(
			Exception ex, WebRequest request) {
		return new ResponseEntity<>(
				new Error(HttpStatus.NOT_FOUND, ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({Exception.class})
	public ResponseEntity<Error> exception(
			Exception ex, WebRequest request) {
		return new ResponseEntity<>(
				new Error(HttpStatus.BAD_REQUEST, ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

}