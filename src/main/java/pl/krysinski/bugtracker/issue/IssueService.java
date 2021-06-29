package pl.krysinski.bugtracker.issue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.krysinski.bugtracker.mail.Mail;
import pl.krysinski.bugtracker.mail.MailService;
import pl.krysinski.bugtracker.person.Person;
import pl.krysinski.bugtracker.person.PersonService;
import pl.krysinski.bugtracker.utils.MarkdownParserUtils;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class IssueService {

    private final PersonService personService;
    private final MarkdownParserUtils markdownParserUtils;
    private final MailService mailService;
    private final IssueRepository issueRepository;

    @Autowired
    public IssueService(PersonService personService, MarkdownParserUtils markdownParserUtils, MailService mailService, IssueRepository issueRepository) {
        this.personService = personService;
        this.markdownParserUtils = markdownParserUtils;
        this.mailService = mailService;
        this.issueRepository = issueRepository;
    }

    public String initMailContent(Issue issue){
        String dateCreated = issue.getDateCreated().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "r.";
        String dateDelete = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "r.";
        String content = "Twoje zgłoszenie " + "'"+ issue.getName() + "'" + " (dot. projektu: " + issue.getProject().getName() + "), utworzone w dniu: " + dateCreated + " zostało usunięte w dniu: " + dateDelete;
        return content;
    }

    void addCreatorToIssue(Issue issue, Principal principal) {
        Optional<Person> loggedUser = personService.getLoggedUser(principal);
        loggedUser.ifPresent(issue::setCreator);
    }

    void markdownParser(Issue issue) {
        issue.setHtml(markdownParserUtils.markdownToHTML(issue.getDescription()));
    }

    void sendEmailAboutDeleteIssue(Issue issue, String emailAddress) {
        if (!emailAddress.isEmpty()){
            String subject = "Usunięto Twoje zadanie";
            mailService.send(new Mail(emailAddress, subject, initMailContent(issue)));
            log.info("We send an email about closed issue to: " + emailAddress);
            log.debug("Send an email about closed issue to: {}", emailAddress);
        }else
        {
            log.info("We didn't send an email about closed issue. We couldn't find the email address of the user with the given login: " + issue.getCreator().getUsername());
            log.debug("Didn't send an email about closed issue user with the given login: {}", issue.getCreator().getUsername());
        }
    }

//    @CacheEvict(value = "issues", allEntries = true)
    public void save(Issue issue) {
        issueRepository.save(issue);
    }

//    @CacheEvict(value = "issues", allEntries = true)
    public void delete(Issue issue) {
        issueRepository.delete(issue);
    }

//    @Cacheable(value = "issues")
    public List<Issue> findAll(IssueFilter issueFilter){
        return issueRepository.findAll(issueFilter.buildQuery());
    }


//    void sendEmailAboutMoreThenCrtiticalIssue(Issue issue, String emailAddress) {
//        if (!emailAddress.isEmpty()){
//            String subject = "Zostałeś przypisany do zgłoszenia";
//            String content = "Zostałeś wybrany do wykonania zgłoszenia"
//            mailService.send(new Mail(emailAddress, subject, initMailContent(issue)));
//            log.info("We send an email about closed issue to: " + emailAddress);
//            log.debug("Send an email about closed issue to: {}", emailAddress);
//        }else
//        {
//            log.info("We didn't send an email about closed issue. We couldn't find the email address of the user with the given login: " + issue.getCreator().getUsername());
//            log.debug("Didn't send an email about closed issue user with the given login: {}", issue.getCreator().getUsername());
//        }
//    }
}
