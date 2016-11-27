package com.yardsale.user.mapper

import com.yardsale.user.dao.entity.UserEntity
import com.yardsale.user.domain.User
import spock.lang.Specification

class UserMapperTest
        extends Specification {

    private mapper


    void setup() {
        mapper = new UserMapper()
    }


    def "Map User to UserEntity When User is null Then return null entity"() {
        setup:

        when:
        def userEntity = mapper.map(null)

        then:
        userEntity == null
    }

    def "Map User to UserEntity When User has all fields Then mapped successfully"() {
        setup:
        def user = createUser()

        when:
        def userEntity = mapper.map(user)

        then:
        userEntity.getId().equals(user.getId()) &&
        userEntity.getEmail().equals(user.getEmail()) &&
        userEntity.getName().equals(user.getName()) &&
        userEntity.getAvatarUrl().equals(user.getAvatarUrl()) &&
        userEntity.getPassword().equals(user.getPassword()) &&
        userEntity.getRegisterDate().equals(user.getRegisterDate())
    }

    def "Reverse Map UserEntity to User When UserEntity has all fields Then mapped successfully"() {
        setup:
        def userEntity = createUserEntity()

        when:
        def user = mapper.mapReverse(userEntity)

        then:
        user.getId().equals(userEntity.getId()) &&
        user.getEmail().equals(userEntity.getEmail()) &&
        user.getName().equals(userEntity.getName()) &&
        user.getAvatarUrl().equals(userEntity.getAvatarUrl()) &&
        user.getPassword().equals(userEntity.getPassword()) &&
        user.getRegisterDate().equals(userEntity.getRegisterDate())
    }


    private UserEntity createUserEntity() {

        def userEntity = new UserEntity()
        userEntity.setId(100)
        userEntity.setEmail("user@mail.com")
        userEntity.setPassword("pswd")
        userEntity.setName("name")
        userEntity.setAvatarUrl("url")

        userEntity
    }

    private User createUser() {

        def user = new User()
        user.setId(100)
        user.setEmail("user@mail.com")
        user.setPassword("pswd")
        user.setName("name")
        user.setAvatarUrl("url")

        user
    }
}
