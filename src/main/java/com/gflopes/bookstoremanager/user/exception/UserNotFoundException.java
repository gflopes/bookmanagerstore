package com.gflopes.bookstoremanager.user.exception;

import javax.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(Long id) {
        super(String.format("User with id %s not found", id));
    }

    public UserNotFoundException(String username) {
        super(String.format("User with id %s not found", username));
    }
}
