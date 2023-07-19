package com.empik.user.repository;

import com.empik.user.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>, UserRepositoryCustom {

    Optional<UserEntity> findByLogin(String login);
}
