package com.github.pnowy.store.common;

import com.github.pnowy.store.order.web.IllegalDateRangeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
@SuppressWarnings("unused")
public class ExceptionTranslator {

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Entity not found")
    @ExceptionHandler(EntityNotFoundException.class)
    public void entityNotFound() {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The date to must be after date from.")
    @ExceptionHandler(IllegalDateRangeException.class)
    public void illegalDateRage() {}

}
