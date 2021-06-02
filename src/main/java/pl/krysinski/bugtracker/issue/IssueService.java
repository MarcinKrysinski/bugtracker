package pl.krysinski.bugtracker.issue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.krysinski.bugtracker.person.Person;
import pl.krysinski.bugtracker.person.PersonService;
import pl.krysinski.bugtracker.project.Project;
import pl.krysinski.bugtracker.utils.MarkdownParserUtils;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class IssueService {

    private final PersonService personService;
    private final MarkdownParserUtils markdownParserUtils;

    @Autowired
    public IssueService(PersonService personService, MarkdownParserUtils markdownParserUtils) {
        this.personService = personService;
        this.markdownParserUtils = markdownParserUtils;
    }

    public String initMailContent(Issue issue){
        String dateCreated = issue.getDateCreated().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "r.";
        String dateClosed = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "r.";
        String content = "Twoje zgłoszenie, utworzone w dniu: " + dateCreated + " zostało zamknięte w dniu: " + dateClosed;
        return content;
    }

    void addCreatorToIssue(Issue issue, Principal principal) {
        Optional<Person> loggedUser = personService.getLoggedUser(principal);
        loggedUser.ifPresent(issue::setCreator);
    }

    void markdownParser(Issue issue) {
        issue.setHtml(markdownParserUtils.markdownToHTML(issue.getDescription()));
    }
}
