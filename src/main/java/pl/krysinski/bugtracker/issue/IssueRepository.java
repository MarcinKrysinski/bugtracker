package pl.krysinski.bugtracker.issue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.krysinski.bugtracker.enums.Status;
import pl.krysinski.bugtracker.person.Person;
import pl.krysinski.bugtracker.project.Project;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long>, JpaSpecificationExecutor<Issue> {

    List<Issue> findAllByProject(Project project);

    Integer countAllByStatusIs(Status status);

    List<Issue> findAllByStatus(Status status);

    List<Issue> findAllByStatusAndAssignee(Status status, Optional<Person> assignee);

    List<Issue> findAllByStatusOrderByPriority(Status status);

    @Query(value = "SELECT COUNT(*) FROM issue WHERE status=:#{#status?.name()} AND date_created > CLOCK_TIMESTAMP() - ( :interval)\\:\\:INTERVAL", nativeQuery = true)
    Integer countAllByStatusIsAndCreatedEarlierThan(@Param("status") Status status, @Param("interval") String interval);

    @Query(value = "SELECT * FROM issue WHERE date_created > CLOCK_TIMESTAMP() - ( :interval)\\:\\:INTERVAL", nativeQuery = true)
    Iterable<Issue> findIssuesCreatedEarlierThan(@Param("interval") String interval);
}
