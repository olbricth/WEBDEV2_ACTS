package com.M.act1.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(com.M.act1.exceptions.CarNotFoundException.class)
    public String handleCarNotFound(com.M.act1.exceptions.CarNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage()); // matches your HTML
        return "error"; // Loads error.html
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("errorMessage", "An unexpected error occurred: " + ex.getMessage());
        return "error";
    }
}
