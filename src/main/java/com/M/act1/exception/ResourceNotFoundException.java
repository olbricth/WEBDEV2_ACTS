package com.M.act1.exception;

public class ResourceNotFoundException extends RuntimeException {

    // Constructor that takes the name of the resource and its ID
    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " with ID " + id + " not found.");
    }

    // You can add another constructor for custom messages if needed
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
