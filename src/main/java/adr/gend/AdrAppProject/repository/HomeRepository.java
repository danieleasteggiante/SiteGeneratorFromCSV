package adr.gend.AdrAppProject.repository;

import adr.gend.AdrAppProject.entity.Home;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeRepository extends JpaRepository<Home, Integer> {
}
