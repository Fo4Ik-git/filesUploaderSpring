package com.fo4ik.webFilesSaver.repo;

import com.fo4ik.webFilesSaver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
