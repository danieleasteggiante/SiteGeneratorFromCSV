package adr.gend.AdrAppProject.repository;

import adr.gend.AdrAppProject.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<Post> findByLink(String slug);
    @Query("SELECT COUNT(p.id) FROM Post p ")
    Integer countAllRows();
}
