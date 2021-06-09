package pl.krysinski.bugtracker.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.krysinski.bugtracker.enums.Status;
import pl.krysinski.bugtracker.issue.Issue;
import pl.krysinski.bugtracker.issue.IssueRepository;

import java.util.List;
import java.util.concurrent.Callable;

@Service
public class ReportService implements Callable<String> {

    private final IssueRepository issueRepository;

    @Autowired
    public ReportService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    public String prepareReport(){

        Integer toDoIssuesAmount = issueRepository.countAllByStatusIs(Status.TODO);
        Integer doneIssuesAmount = issueRepository.countAllByStatusIs(Status.DONE);

        List<Issue> toDoIssues = issueRepository.findAllByStatusOrderByPriority(Status.TODO);
        StringBuilder issues = new StringBuilder();
        for (Issue issue: toDoIssues) {
            issues.append(issue).append("\n\n");
        }
        StringBuilder mailText = new StringBuilder();
        mailText.append("Raport z ostaniego miesiąca:\n\n")
                .append("- ilość otwartych zadań/błedów: ")
                .append(toDoIssuesAmount)
                .append("\n")
                .append("- ilość zamkniętych zadań/błedów: ")
                .append(doneIssuesAmount)
                .append("\n\n")
                .append("Lista otwartych zadań:\n\n")
                .append(issues)
                .append("\n\n")
                .append("Wiadomość wysłano automatycznie z platformy Bugtracker");
        String readyMailText = mailText.toString();
        return readyMailText;
    }



    @Override
    public String call() {
        return prepareReport();
    }
}
