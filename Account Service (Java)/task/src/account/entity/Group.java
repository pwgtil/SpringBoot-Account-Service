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

    @ManyToMany(mappedBy = "user_groups")
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

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
