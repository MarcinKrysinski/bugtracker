package pl.krysinski.bugtracker.index;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.krysinski.bugtracker.enums.Status;
import pl.krysinski.bugtracker.issue.IssueRepository;
import pl.krysinski.bugtracker.person.Person;
import pl.krysinski.bugtracker.person.PersonRepository;
import pl.krysinski.bugtracker.security.SecurityService;

import java.util.Optional;

@Controller
@Slf4j
@RequestMapping(value={"/","/kanban"})
public class KanbanController {

    private final IssueRepository issueRepository;
    private final PersonRepository personRepository;
    private final SecurityService securityService;

    @Autowired
    public KanbanController(IssueRepository issueRepository, PersonRepository personRepository, SecurityService securityService) {
        this.issueRepository = issueRepository;
        this.personRepository = personRepository;
        this.securityService = securityService;
    }

    @GetMapping
    public String kanbanTable(Model model){
        String usernameLoggedPerson = securityService.getUsernameLoggedUser();
        Optional<Person> loggedPerson = personRepository.findByUsername(usernameLoggedPerson);

        model.addAttribute("TODO", issueRepository.findAllByStatusAndAssignee(Status.TODO, loggedPerson));
        model.addAttribute("DOING", issueRepository.findAllByStatusAndAssignee(Status.DOING, loggedPerson));
        model.addAttribute("DONE", issueRepository.findAllByStatusAndAssignee(Status.DONE, loggedPerson));

        log.debug("Getting kanban table: {}", model);
        return "table/kanban";
    }
}
