package com.server_chat.chat.repository;

import com.server_chat.chat.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
//    Boolean existsByUsername(String username);
//    Boolean existsByEmail(String email);
}
