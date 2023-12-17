package adr.gend.AdrAppProject.repository;

import adr.gend.AdrAppProject.entity.Image;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.repository.CrudRepository;

public interface ImageCRUDRepository extends CrudRepository<Image, Integer> {
    Integer deleteByName(String name);
}

