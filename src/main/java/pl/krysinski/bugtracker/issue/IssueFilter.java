package pl.krysinski.bugtracker.issue;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.krysinski.bugtracker.enums.Priority;
import pl.krysinski.bugtracker.enums.Status;
import pl.krysinski.bugtracker.enums.Type;
import pl.krysinski.bugtracker.person.Person;
import pl.krysinski.bugtracker.project.Project;


@Getter
@Setter
@NoArgsConstructor
public class IssueFilter {

    Project project;
    Type type;
    Status status;
    Priority priority;
    Person assignee;

}
