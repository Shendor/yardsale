package com.yardsale.domain;

import java.time.LocalDate;

public class BaseUser {

    private long id;
    private String name;
    private String avatarUrl;
    private LocalDate registerDate;


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public BaseUser() {
        registerDate = LocalDate.now();
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getAvatarUrl() {
        return avatarUrl;
    }


    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }


    public LocalDate getRegisterDate() {
        return registerDate;
    }


    public void setRegisterDate(LocalDate registerDate) {
        this.registerDate = registerDate;
    }

}
