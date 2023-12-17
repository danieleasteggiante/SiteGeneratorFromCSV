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
public class Home {
    @Id
    Integer id;
    String sectionname;
    String sectioncontent;
    Boolean visible;

}