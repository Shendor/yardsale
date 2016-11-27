package com.yardsale.user.service;

import java.util.Optional;

import com.yardsale.user.domain.User;

public interface UserService {

    void createUser(User user);

    Optional<User> getUserByEmail(String email);
}
