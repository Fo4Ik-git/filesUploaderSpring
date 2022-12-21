package com.fo4ik.webFilesSaver.repo;

import com.fo4ik.webFilesSaver.model.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface FileRepo extends JpaRepository<FileModel, Long> {

    FileModel findById(long id);
    FileModel findAllByUser(long user_id);

    //@Query("select f from FileModel f where f.user.user_id = ?1")
    List<FileModel> findAllByUserId(Long id);



    FileModel findByName(String name);

    FileModel findByPath(String Path);
}
