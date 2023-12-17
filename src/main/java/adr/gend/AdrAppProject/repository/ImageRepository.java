package adr.gend.AdrAppProject.repository;

import adr.gend.AdrAppProject.entity.Image;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    List<Image> findFirst5ByOrderById();
}
