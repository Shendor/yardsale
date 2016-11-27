package com.yardsale.user.dao;

import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

import com.yardsale.user.dao.entity.UserEntity;

public class InMemoryUserDaoImpl implements UserDao {

    private SortedMap<Long, UserEntity> users = new TreeMap<>();


    @Override
    public long insertUser(UserEntity userEntity) {
        long nextId = users.get(users.lastKey()).getId() + 1;
        userEntity.setId(nextId);

        users.put(nextId, userEntity);

        return nextId;
    }


    @Override
    public boolean isEmailExist(String email) {
        return users.values().stream()
                    .filter(user -> user.getEmail().equals(email))
                    .count() != 0;
    }


    @Override
    public Optional<UserEntity> findUserByEmail(String email) {
        return users.values().stream()
                    .filter(user -> user.getEmail().equals(email))
                    .findFirst();
    }
}
