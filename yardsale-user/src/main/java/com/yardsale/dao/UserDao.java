package com.yardsale.dao;

import java.util.Optional;

import com.yardsale.dao.entity.UserEntity;

public interface UserDao {

    long insertUser(UserEntity userEntity);


    boolean isEmailExist(String email);


    Optional<UserEntity> findUserByEmail(String email);
}
