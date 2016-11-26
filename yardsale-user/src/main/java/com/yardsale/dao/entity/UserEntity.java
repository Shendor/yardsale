package com.yardsale.dao.entity;

import java.time.LocalDate;

public class UserEntity {

    private long id;
    private String password;
    private String email;
    private String avatarUrl;
    private String name;
    private LocalDate registerDate;


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getAvatarUrl() {
        return avatarUrl;
    }


    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public LocalDate getRegisterDate() {
        return registerDate;
    }


    public void setRegisterDate(LocalDate registerDate) {
        this.registerDate = registerDate;
    }
}
