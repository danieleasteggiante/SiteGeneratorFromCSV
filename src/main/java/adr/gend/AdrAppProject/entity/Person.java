package adr.gend.AdrAppProject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    @Id
    Integer id;
    String firstname;
    String lastname;
    String title;
    String cv;
    String photo;
}
