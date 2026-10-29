package com.example.employeemanagement.exception;

public class DuplicateEmailException
        extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("Employee already exists with email: " + email);
    }
}