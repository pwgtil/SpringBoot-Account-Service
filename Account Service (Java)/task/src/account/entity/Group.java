package account.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "principle_groups")
public class Group {
    /*
     * Properties
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @ManyToMany(mappedBy = "userGroups")
    private Set<User> users;

    /*
     * Constructors
     * */
    public Group(String code) {
        this.code = code;
    }
}
