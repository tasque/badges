package org.badges.api.controller.error;

import lombok.extern.slf4j.Slf4j;
import org.badges.api.controller.BadgesController;
import org.badges.security.TenantException;
import org.badges.service.BadgeAssignmentValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice(basePackageClasses = BadgesController.class)
@Slf4j
public class RestHandler {

    @ExceptionHandler(BadgeAssignmentValidationException.class)
    public ResponseEntity validationException(BadgeAssignmentValidationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(TenantException.class)
    public ResponseEntity tenantException(TenantException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleNotFoundException(EntityNotFoundException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity handle(EntityNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
