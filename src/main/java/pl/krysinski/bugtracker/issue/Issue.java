package pl.krysinski.bugtracker.issue;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import pl.krysinski.bugtracker.comment.Comment;
import pl.krysinski.bugtracker.enums.Priority;
import pl.krysinski.bugtracker.enums.Status;
import pl.krysinski.bugtracker.enums.Type;
import pl.krysinski.bugtracker.person.Person;
import pl.krysinski.bugtracker.project.Project;
import pl.krysinski.bugtracker.validators.IssueMandatoryAssigneeField;
import pl.krysinski.bugtracker.validators.IssueMandatoryProjectField;
import pl.krysinski.bugtracker.validators.IssueMandatoryTitle;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@IssueMandatoryProjectField
@IssueMandatoryAssigneeField
@IssueMandatoryTitle
public class Issue implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Audited
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.TODO;
    @Audited
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.NORMAL;
    @Audited
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type= Type.BUG;
    @Audited
    @Column(nullable = false, length = 120)
    private String name;
    @Column(columnDefinition = "text")
    private String description;
//    @Column(unique = true, length = 20)
//    private String code; //issue number? short name? relacja?
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private Person creator;
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name = "assignee_id", nullable = false)
    private Person assignee;
    @Column(nullable = false)
    private final LocalDate dateCreated = LocalDate.now();
//    private LocalDate lastUpdate; // czy ja w końcu to wykorzytsuje? nie!!!
    @OneToMany(mappedBy = "issue")
    private List<Comment> comments;
    @Column(columnDefinition = "text")
    private String html;

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", status=" + status +
                ", priority=" + priority +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
