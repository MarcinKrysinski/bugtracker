package pl.krysinski.bugtracker.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.krysinski.bugtracker.issue.Issue;
import pl.krysinski.bugtracker.person.Person;

import javax.persistence.*;
import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Person author;
    private String content;
    private final Date dateCreated = new Date();
    @ManyToOne
    @JoinColumn(name = "issue_id", nullable = false)
    private Issue issue;

}
