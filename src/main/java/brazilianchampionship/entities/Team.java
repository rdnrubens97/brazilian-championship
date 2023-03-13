package brazilianchampionship.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 20)
    private String name;
    @Column(length = 4)
    private String initials;
    @Column(length = 2)
    private String ufEstate;
    @Column(length = 25)
    private String stadium;

}
