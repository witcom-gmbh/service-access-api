package de.witcom.itsm.serviceaccess.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import de.witcom.itsm.serviceaccess.dto.ErrorMessageDTO;
import de.witcom.itsm.serviceaccess.exception.BadRequestException;
import de.witcom.itsm.serviceaccess.exception.NotFoundException;
import de.witcom.itsm.serviceaccess.exception.PersistenceException;

@ControllerAdvice
public class ExceptionTranslator {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionTranslator.class);

	@ExceptionHandler({ RuntimeException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErrorMessageDTO> handleRunTimeException(RuntimeException e) {
		return error(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

	@ExceptionHandler({ PersistenceException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErrorMessageDTO> handlePersistenceException(PersistenceException e) {
		return error(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

	@ExceptionHandler({ BadRequestException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErrorMessageDTO> handleBadRequestException(BadRequestException e) {
		return error(HttpStatus.BAD_REQUEST, e);
    }
	
    @ExceptionHandler({ NotFoundException.class })
    public ResponseEntity<ErrorMessageDTO> handleNotFoundException(NotFoundException e) {
		return error(HttpStatus.NOT_FOUND, e);
    }

	private ResponseEntity<ErrorMessageDTO> error(HttpStatus status, Exception e) {
		LOGGER.error("Exception : ", e);
		return ResponseEntity.status(status).body(new ErrorMessageDTO(UUID.randomUUID().toString(), e.getMessage()));
    }
}