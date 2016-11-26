package com.yardsale.service;

import java.util.Optional;

import com.yardsale.domain.User;

public interface UserService {

    void createUser(User user);

    Optional<User> getUserByEmail(String email);
}
