package adr.gend.AdrAppProject.repository;

import adr.gend.AdrAppProject.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    @Query("SELECT COUNT(p.id) FROM Person p ")
    Integer countAllRows();
}

