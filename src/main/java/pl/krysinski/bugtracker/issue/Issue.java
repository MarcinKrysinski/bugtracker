//package pl.krysinski.bugtracker.issue;
//
//import pl.krysinski.bugtracker.comment.Comment;
//import pl.krysinski.bugtracker.enums.Priority;
//import pl.krysinski.bugtracker.enums.Status;
//import pl.krysinski.bugtracker.enums.Type;
//import pl.krysinski.bugtracker.person.Person;
//import pl.krysinski.bugtracker.project.Project;
//
//import javax.persistence.*;
//import java.time.LocalDate;
//import java.util.List;
//
////@Entity
//public class Issue {
//
//    @Id
//    @GeneratedValue
//    private Long id;
//    @Column(nullable = false)
//    private Status status;
//    @Column(nullable = false)
//    private Priority priority;
//    @Column(nullable = false)
//    private Type type;
//    @Column(nullable = false, unique = true, length = 120)
//    private String name;
//    private String description;
//    @Column(unique = true, length = 20)
//    private String code; //issue number?
//    @ManyToOne
//    @JoinColumn(name = "project_id", nullable = false)
//    private Project project;
//    @OneToOne
//    @JoinColumn(name = "creator_id", nullable = false)
//    private Person creator;
//    @OneToOne
//    @JoinColumn(name = "assignee_id", nullable = false)
//    private Person assignee;
//    @Column(nullable = false)
//    private final LocalDate dateCreated = LocalDate.now();
//    private LocalDate lastUpdate;
//    @OneToMany(mappedBy = "issue")
//    private List<Comment> comments;
//

//}
