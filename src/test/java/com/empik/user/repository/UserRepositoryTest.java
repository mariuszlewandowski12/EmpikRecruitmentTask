package com.empik.user.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;

import com.empik.user.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Sql("classpath:example_users.sql")
    void whenInitializeBySqlScript_shouldFindAllUsers() {

        List<UserEntity> users = userRepository.findAll();

        assertThat(users, hasSize(3));
        assertTrue(users.stream().filter(u->u.getLogin().equals("user1") &&u.getRequestCount() ==1).findFirst().isPresent());
        assertTrue(users.stream().filter(u->u.getLogin().equals("user2") &&u.getRequestCount() ==15).findFirst().isPresent());
        assertTrue(users.stream().filter(u->u.getLogin().equals("user3") &&u.getRequestCount() ==88).findFirst().isPresent());
    }

    @Test
    @Sql("classpath:example_users.sql")
    void whenInitializeBySqlScript_shouldFindByLogin() {

        Optional<UserEntity> user = userRepository.findByLogin("user2");

        assertTrue(user.isPresent());
        assertTrue(user.get().getLogin().equals("user2"));
        assertTrue(user.get().getRequestCount() == 15);
    }

    @Test
    @Sql("classpath:example_users.sql")
    void whenInitializeBySqlScript_shouldFindByLoginReturnEmpty() {

        Optional<UserEntity> user = userRepository.findByLogin("user4");

        assertFalse(user.isPresent());
    }

    @Test
    @Sql("classpath:example_users.sql")
    void whenInitializeBySqlScript_shouldAddOrIncrementIncrementExistingUser() {

        userRepository.addOrIncreaseUsageCount("user2");

        Optional<UserEntity> user = userRepository.findByLogin("user2");

        assertTrue(user.isPresent());
        assertTrue(user.get().getLogin().equals("user2"));
        assertTrue(user.get().getRequestCount() == 16);
    }

    @Test
    @Sql("classpath:example_users.sql")
    void whenInitializeBySqlScript_shouldAddOrIncrementAddUser() {

        userRepository.addOrIncreaseUsageCount("user4");

        Optional<UserEntity> user = userRepository.findByLogin("user4");

        assertTrue(user.isPresent());
        assertTrue(user.get().getLogin().equals("user4"));
        assertTrue(user.get().getRequestCount() == 1);
    }

    @Test
    @Sql("classpath:example_users.sql")
    void whenInitializeBySqlScript_shouldAddOrIncrementIncrement() {

        userRepository.addOrIncreaseUsageCount("user4");
        userRepository.addOrIncreaseUsageCount("user4");
        userRepository.addOrIncreaseUsageCount("user4");
        userRepository.addOrIncreaseUsageCount("user4");


        Optional<UserEntity> user = userRepository.findByLogin("user4");

        assertTrue(user.isPresent());
        assertTrue(user.get().getLogin().equals("user4"));
        assertTrue(user.get().getRequestCount() == 4);
    }
}