package com.yardsale.service;


import java.util.Optional;

import com.yardsale.dao.UserDao;
import com.yardsale.dao.entity.UserEntity;
import com.yardsale.domain.User;
import com.yardsale.exception.EmailExistException;
import com.yardsale.mapper.Mapper;
import org.apache.log4j.Logger;

public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);

    private UserDao userDao;
    private Mapper<User, UserEntity> userMapper;


    public void createUser(User user) {
        if (user != null) {
            if (!userDao.isEmailExist(user.getEmail())) {
                UserEntity userEntity = userMapper.map(user);

                long id = userDao.insertUser(userEntity);
                user.setId(id);
                LOGGER.info(String.format("User has been created with %s email", user.getEmail()));
            } else {
                throw new EmailExistException(String.format("Can't create a new user: Email %s already exist", user.getEmail()));
            }
        }
    }


    @Override
    public Optional<User> getUserByEmail(String email) {
        Optional<UserEntity> userEntity = userDao.findUserByEmail(email);
        User user = null;
        if (userEntity.isPresent()) {
            user = userMapper.mapReverse(userEntity.get());
        }
        return Optional.ofNullable(user);
    }


    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }


    public void setUserMapper(Mapper<User, UserEntity> userMapper) {
        this.userMapper = userMapper;
    }
}
