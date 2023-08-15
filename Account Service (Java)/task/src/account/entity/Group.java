package account.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
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

    public Group() {
    }

    /*
     * Getters Setters
     * */
    public String getCode() {
        return code;
    }

}
