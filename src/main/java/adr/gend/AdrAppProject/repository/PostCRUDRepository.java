package adr.gend.AdrAppProject.repository;

import adr.gend.AdrAppProject.entity.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostCRUDRepository extends CrudRepository<Post, Integer> {
    Integer deleteByLink(String link);

}

