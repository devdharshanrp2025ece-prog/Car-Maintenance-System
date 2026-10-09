package com.carmaintenance.exception;

public final class GlobalExceptionHandler {
    private GlobalExceptionHandler() {}

    public static void handle(Exception e) {
        if (e instanceof ValidationException) {
            System.out.println("Invalid input: " + e.getMessage());
        } else if (e instanceof ResourceNotFoundException) {
            System.out.println("Not found: " + e.getMessage());
        } else if (e instanceof DuplicateResourceException) {
            System.out.println("Duplicate record: " + e.getMessage());
        } else if (e instanceof InvalidOperationException) {
            System.out.println("Operation rejected: " + e.getMessage());
        } else if (e instanceof DatabaseException) {
            System.out.println("Database error: " + e.getMessage());
        } else {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}
