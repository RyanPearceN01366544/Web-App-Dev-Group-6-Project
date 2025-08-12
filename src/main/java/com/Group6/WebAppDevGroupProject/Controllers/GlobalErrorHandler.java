package com.Group6.WebAppDevGroupProject.Controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalErrorHandler {
    @ExceptionHandler(Exception.class)
    public String handle(Exception e_, Model model_) {
        e_.printStackTrace();
        model_.addAttribute("msg", e_.getMessage());
        return "error";
    }
}
