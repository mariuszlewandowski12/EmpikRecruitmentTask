package com.empik.user.repository;

import com.empik.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @Autowired
    @Lazy
    private UserRepository userRepository;

    @Transactional
    public void addOrIncreaseUsageCount(String login) {

        UserEntity user = userRepository.findByLogin(login).orElse(UserEntity.builder().login(login).RequestCount(0).build());
        user.setRequestCount(user.getRequestCount()+1);
        userRepository.save(user);
    }
}
