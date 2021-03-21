package pl.krysinski.bugtracker.issue;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Issue {

    @Id
    Long id;
}
