package com.yardsale.service

import com.yardsale.dao.UserDao
import com.yardsale.dao.entity.UserEntity
import com.yardsale.domain.User
import com.yardsale.exception.EmailExistException
import com.yardsale.mapper.Mapper
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class UserServiceImplTest
        extends Specification {

    private userService
    private mockUserDao
    private Mapper mockUserMapper


    def setup() {
        userService = new UserServiceImpl()
        mockUserDao = Mock(UserDao.class)
        mockUserMapper = Mock(Mapper.class)

        userService.setUserDao(mockUserDao);
        userService.setUserMapper(mockUserMapper);
    }


    def "Create User When no user provided Then nothing happens"() {
        setup:

        expect:
        userService.createUser(null);
    }


    def "Create User When user provided Then correct user inserted"() {
        setup:
        def expectedId = 1L
        def user = new User();
        def userEntity = new UserEntity()

        and:
        mockUserMapper.map(user) >> userEntity
        mockUserDao.insertUser({ it == userEntity }) >> expectedId

        when:
        userService.createUser(user)

        then:
        user.getId() == expectedId
    }


    def "Create User When email already exist Then throw an exception"() {
        setup:
        def user = new User();
        user.setEmail("user1@mail.com")

        and:
        mockUserDao.isEmailExist(user.getEmail()) >> true

        when:
        userService.createUser(user)

        then:
        0 * mockUserDao.insertUser(_)
        thrown(EmailExistException.class)
    }


    def "Load User By Email When User doesn't exist Then return null"() {
        setup:
        def email = "user1@mail.com"

        and:
        mockUserDao.findUserByEmail(email) >> Optional.empty()

        when:
        def user = userService.getUserByEmail(email)

        then:
        !user.isPresent()
    }

    def "Load User By Email When User exist Then return user"() {
        setup:
        def email = "user1@mail.com"
        def expectedUser = new User()
        def expectedUserEntity = Optional.of(new UserEntity())

        and:
        mockUserDao.findUserByEmail(email) >> expectedUserEntity
        mockUserMapper.mapReverse(expectedUserEntity.get()) >> expectedUser

        when:
        def user = userService.getUserByEmail(email)

        then:
        user.isPresent()
        expectedUser == user.get();
    }
}
