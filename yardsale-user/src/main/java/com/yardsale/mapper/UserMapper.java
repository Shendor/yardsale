package com.yardsale.mapper;

import com.yardsale.dao.entity.UserEntity;
import com.yardsale.domain.User;

public class UserMapper implements Mapper<User, UserEntity> {

    @Override
    public UserEntity map(User user) {
        if (user != null) {
            UserEntity userEntity = new UserEntity();
            userEntity.setId(user.getId());
            userEntity.setEmail(user.getEmail());
            userEntity.setName(user.getName());
            userEntity.setPassword(user.getPassword());
            userEntity.setAvatarUrl(user.getAvatarUrl());
            userEntity.setRegisterDate(user.getRegisterDate());
            return userEntity;
        }
        return null;
    }


    @Override
    public User mapReverse(UserEntity userEntity) {
        if (userEntity != null) {
            User user = new User();
            user.setId(userEntity.getId());
            user.setEmail(userEntity.getEmail());
            user.setName(userEntity.getName());
            user.setPassword(userEntity.getPassword());
            user.setAvatarUrl(userEntity.getAvatarUrl());
            user.setRegisterDate(userEntity.getRegisterDate());

            return user;
        }
        return null;
    }
}
