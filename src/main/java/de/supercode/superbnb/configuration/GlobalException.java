package de.supercode.superbnb.configuration;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(Exception.class)
    public void handlerException(Exception ex){
        ex.printStackTrace();
    }

}
