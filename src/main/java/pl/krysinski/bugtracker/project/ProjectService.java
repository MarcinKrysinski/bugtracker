package pl.krysinski.bugtracker.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.krysinski.bugtracker.person.Person;
import pl.krysinski.bugtracker.person.PersonService;
import pl.krysinski.bugtracker.utils.MarkdownParserUtils;

import java.security.Principal;
import java.util.Optional;

@Service
public class ProjectService {

    private final PersonService personService;
    private final MarkdownParserUtils markdownParserUtils;

    @Autowired
    public ProjectService(PersonService personService, MarkdownParserUtils markdownParserUtils) {
        this.personService = personService;
        this.markdownParserUtils = markdownParserUtils;
    }


    void addCreatorToProject(Project project, Principal principal) {
        Optional<Person> loggedUser = personService.getLoggedUser(principal);
        loggedUser.ifPresent(project::setCreator);
    }

     void markdownParser(Project project) {
        project.setHtml(markdownParserUtils.markdownToHTML(project.getDescription()));
    }
}
