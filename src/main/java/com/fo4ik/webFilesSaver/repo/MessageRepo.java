package com.fo4ik.webFilesSaver.repo;

import com.fo4ik.webFilesSaver.model.Message;
import org.springframework.data.repository.CrudRepository;

import javax.lang.model.element.Element;
import java.util.List;

public interface MessageRepo extends CrudRepository<Message, Long> {

        List<Message> findByTag(String tag);

}
