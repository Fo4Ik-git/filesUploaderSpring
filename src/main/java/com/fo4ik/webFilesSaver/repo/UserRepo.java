package com.fo4ik.webFilesSaver.repo;

import com.fo4ik.webFilesSaver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@EnableJpaRepositories
@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query(value = "SELECT u.* FROM usr u INNER JOIN ( SELECT user_id FROM user_role WHERE roles =?1 GROUP BY user_id HAVING COUNT(user_id) = 1 ) ur ON u.id = ur.user_id", nativeQuery = true)
    User findByRole(String role);
}
