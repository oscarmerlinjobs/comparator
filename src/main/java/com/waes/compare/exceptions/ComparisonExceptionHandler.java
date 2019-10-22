package com.waes.compare.exceptions;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ComparisonExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComparisonExceptionHandler.class);

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity handleConstraintViolationException(ConstraintViolationException e) {
      LOGGER.warn(e.getMessage());
      return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity handleIllegalArgumentException(IllegalArgumentException e) {
      LOGGER.warn(e.getMessage());
      return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity handleResourceNotFoundException(EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }

}
