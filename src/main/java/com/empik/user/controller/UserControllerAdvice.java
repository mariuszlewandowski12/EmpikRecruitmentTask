package com.empik.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

@Slf4j
@Order(1)
@RestControllerAdvice(basePackages = "com.empik.user")
public class UserControllerAdvice {

    @ExceptionHandler(HibernateException.class)
    public ResponseEntity dbConnectionError(Throwable ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DB access error");
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity restClientError(Throwable ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
    }
}
