package pl.krysinski.bugtracker.person;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import pl.krysinski.bugtracker.authority.Authority;
import pl.krysinski.bugtracker.comment.Comment;
import pl.krysinski.bugtracker.enums.Role;
import pl.krysinski.bugtracker.project.Project;
import pl.krysinski.bugtracker.validators.UniqueUsername;
import pl.krysinski.bugtracker.validators.ValidPasswords;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ValidPasswords
@UniqueUsername
@ToString
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @Size(min = 1, max = 20)
    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false)
    @Size(min = 8, max = 35)
    @NotBlank(message = "Password is mandatory")
    private String password;

    @Transient
    String repeatedPassword;

    @Column(nullable = false)
    @Size(min = 5, max = 20)
    @NotBlank
    private String firstName;

    @Column(nullable = false)
    @Size(min = 5, max = 30)
    @NotBlank
    private String lastName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @NotBlank(message = "Email is mandatory")
    @Pattern(regexp="[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")
    private String email;

    @ColumnDefault("true")
    @Column(nullable = false)
    private Boolean enabled = true;

    private Date dateCreated = new Date();

    @Enumerated(EnumType.STRING)
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "person_authorities",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    Set<Authority> authorities;

//    @OneToMany(mappedBy = "creator")
//    Set<Project> createdProjects;
//    @OneToMany(mappedBy = "issue")
//    List<Comment> comments;

    public Person(String username, String password, String firstName, String lastName, Role role, String email) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.email = email;
    }

    public Person(Long id, String username, String password, String firstName, String lastName, Role role, String email) {
        this.id=id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.email = email;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

//
//    @Override
//    public String toString() {
//        return "Person{" +
//                "id=" + id +
//                ", username='" + username + '\'' +
//                ", password='" + password + '\'' +
//                ", firstName='" + firstName + '\'' +
//                ", lastName='" + lastName + '\'' +
//                ", role=" + role +
//                ", enabled=" + enabled +
//                ", dateCreated=" + dateCreated +
//                ", authorities=" + authorities +
//                '}';
//    }
}
