package pl.krysinski.bugtracker.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.krysinski.bugtracker.enums.Status;
import pl.krysinski.bugtracker.issue.Issue;
import pl.krysinski.bugtracker.issue.IssueRepository;

import java.util.List;

@Service
public class ReportService {

    private final IssueRepository issueRepository;

    @Autowired
    public ReportService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    public String prepareReport(){
        Integer toDoIssuesAmount = issueRepository.countAllByStatusIs(Status.TODO);
        Integer doneIssuesAmount = issueRepository.countAllByStatusIs(Status.DONE);

        List<Issue> toDoIssues = issueRepository.findAllByStatusOrderByPriority(Status.TODO);
        StringBuilder sb = new StringBuilder();
        sb.append("Raport z ostaniego miesiąca:\n")
                .append("- ilość otwartych zadań/błedów: ")
                .append(toDoIssuesAmount)
                .append("\n")
                .append("- ilość zamkniętych zadań/błedów: ")
                .append(doneIssuesAmount)
                .append("\n")
                .append("Lista otwartych zadań:\n")
                .append(toDoIssues);
        String text = sb.toString();
        return text;
    }
}
