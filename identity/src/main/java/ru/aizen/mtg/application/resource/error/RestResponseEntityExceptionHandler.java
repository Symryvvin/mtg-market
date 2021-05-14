package ru.aizen.mtg.application.resource.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.aizen.mtg.application.resource.dto.response.Error;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
		extends ResponseEntityExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

	@ExceptionHandler(value = Exception.class)
	protected ResponseEntity<Object> handleConflict(Exception exception, WebRequest request) {
		logger.error(exception.getMessage(), exception);
		return handleExceptionInternal(
				exception,
				new Error(HttpStatus.BAD_REQUEST.value(), exception.getMessage()),
				new HttpHeaders(),
				HttpStatus.BAD_REQUEST,
				request
		);
	}


}
