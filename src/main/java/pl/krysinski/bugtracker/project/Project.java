package pl.krysinski.bugtracker.project;

import lombok.*;
import pl.krysinski.bugtracker.issue.Issue;
import pl.krysinski.bugtracker.person.Person;
import pl.krysinski.bugtracker.validators.UniqueProjectName;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@UniqueProjectName
//@ToString
public class Project {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @OneToMany(mappedBy = "project")
    private Set<Issue> issues;
    private Boolean enabled = true;
    @Column(nullable = false)
    private final Date dateCreated = new Date();
//    private String code; // short name? relacja?
    @Column(columnDefinition = "text")
    private String description;
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private Person creator;
    @Column(columnDefinition = "text")
    private String html;

    public Project(Long id, String name, Boolean enabled, String description, Person creator, String html) {
        this.id = id;
        this.name = name;
        this.enabled = enabled;
        this.description = description;
        this.creator = creator;
        this.html = html;
    }
}
