package com.yardsale.user.dao;

import java.util.Optional;

import com.yardsale.user.dao.entity.UserEntity;

public interface UserDao {

    long insertUser(UserEntity userEntity);


    boolean isEmailExist(String email);


    Optional<UserEntity> findUserByEmail(String email);
}
