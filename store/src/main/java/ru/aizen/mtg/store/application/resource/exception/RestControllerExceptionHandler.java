package ru.aizen.mtg.store.application.resource.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(RestControllerExceptionHandler.class);

	@ExceptionHandler({StoreNotFountException.class, SingleNotFoundException.class})
	public ResponseEntity<Error> handleNotFound(Exception e, WebRequest request) {
		logger.warn(e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new Error(HttpStatus.NOT_FOUND, e.getMessage()));
	}

	@ExceptionHandler({ForbiddenException.class})
	public ResponseEntity<Error> forbidden(Exception e, WebRequest request) {
		logger.warn(e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(new Error(HttpStatus.FORBIDDEN, e.getMessage()));
	}

	@ExceptionHandler({Exception.class})
	public ResponseEntity<Error> exception(Exception e, WebRequest request) {
		logger.warn(e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new Error(HttpStatus.BAD_REQUEST, e.getMessage()));
	}

}