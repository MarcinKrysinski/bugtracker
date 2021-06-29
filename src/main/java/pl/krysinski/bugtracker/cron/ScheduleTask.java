package pl.krysinski.bugtracker.cron;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.krysinski.bugtracker.enums.Role;
import pl.krysinski.bugtracker.enums.Status;
import pl.krysinski.bugtracker.issue.IssueRepository;
import pl.krysinski.bugtracker.mail.Mail;
import pl.krysinski.bugtracker.mail.MailService;
import pl.krysinski.bugtracker.person.Person;
import pl.krysinski.bugtracker.person.PersonRepository;
import pl.krysinski.bugtracker.report.ReportService;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class ScheduleTask {
    private static Status status = Status.TODO;
    private final IssueRepository issueRepository;
    private final ReportService reportService;
    private final MailService mailService;
    private final PersonRepository personRepository;

    public ScheduleTask(IssueRepository issueRepository, ReportService reportService, MailService mailService, PersonRepository personRepository) {
        this.issueRepository = issueRepository;
        this.reportService = reportService;
        this.mailService = mailService;
        this.personRepository = personRepository;
    }
    @Scheduled(cron = "0 0 12 L * ?")
//    @Scheduled(cron = "0 * * ? * *")
    public void executeIssueCount(){
        LocalDate dateCreated = LocalDate.now();
        String subject = "Raport " + dateCreated;
        String report = reportService.call();
        List<Person> admins =  personRepository.findAllByRole(Role.ADMIN);

        for (Person admin : admins) {
            mailService.send(new Mail(admin.getEmail(), subject, report));
            log.info("We send an email with monthly report: " + admin.getEmail());
            log.debug("Send an email monthly report to: {}", admin.getEmail());
        }

    }
}
