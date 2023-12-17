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
public class Page {
    @Id
    Integer id;
    String name;
    String parent;
    String link;
    String content;

}
