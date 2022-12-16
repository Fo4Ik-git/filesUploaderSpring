package com.fo4ik.webFilesSaver.repo;


import com.fo4ik.webFilesSaver.model.Logo;
import com.fo4ik.webFilesSaver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.awt.*;

@EnableJpaRepositories
@Repository
public interface LogoRepo extends JpaRepository<Logo, Long> {
    Logo findById(long id);
    Logo findByUser(User userFromDb);
}
