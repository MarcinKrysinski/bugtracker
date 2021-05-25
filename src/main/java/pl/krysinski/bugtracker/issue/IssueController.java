package pl.krysinski.bugtracker.issue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.krysinski.bugtracker.enums.Priority;
import pl.krysinski.bugtracker.enums.Status;
import pl.krysinski.bugtracker.enums.Type;
import pl.krysinski.bugtracker.mail.Mail;
import pl.krysinski.bugtracker.mail.MailService;
import pl.krysinski.bugtracker.person.Person;
import pl.krysinski.bugtracker.person.PersonRepository;
import pl.krysinski.bugtracker.person.PersonService;
import pl.krysinski.bugtracker.project.ProjectRepository;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/issues")
public class IssueController {

    private final IssueRepository issueRepository;
    private final PersonRepository personRepository;
    private final ProjectRepository projectRepository;
    private final PersonService personService;
    private final MailService mailService;
    private final IssueService issueService;

    @Autowired
    public IssueController(IssueRepository issueRepository, PersonRepository personRepository, ProjectRepository projectRepository, PersonService personService, MailService mailService, IssueService issueService) {
        this.issueRepository = issueRepository;
        this.personRepository = personRepository;
        this.projectRepository = projectRepository;
        this.personService = personService;
        this.mailService = mailService;
        this.issueService = issueService;
    }

    @GetMapping
    public String issues(@ModelAttribute IssueFilter issueFilter, Model model) {
        model.addAttribute("issues", issueRepository.findAll(issueFilter.buildQuery()));
        model.addAttribute("assignedPerson", personRepository.findAll());
        model.addAttribute("projects", projectRepository.findAll());
        model.addAttribute("filter", issueFilter);
        model.addAttribute("types", Type.values());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("priorities", Priority.values());

        return "issue/issues";
    }

    @GetMapping("/create")
    public String showIssueForm(Model model) {
        model.addAttribute("persons", personRepository.findAll());
        model.addAttribute("projects", projectRepository.findAll());
        model.addAttribute("types", Type.values());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("issue", new Issue());
        return "issue/add-issue";
    }

    @PostMapping("/save")
    public String save(Issue issue, BindingResult result, Principal principal) {
        if (result.hasErrors()){
            return "issue/add-issue";
        }
        Optional<Person> loggedUser = personService.getLoggedUser(principal);
        loggedUser.ifPresent(issue::setCreator);
        issueRepository.save(issue);

        return "redirect:/issues";
    }

    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Issue issue = issueRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid issue id: " + id));
        model.addAttribute("issue", issue);
        model.addAttribute("persons", personRepository.findAll());
        model.addAttribute("projects", projectRepository.findAll());
        model.addAttribute("types", Type.values());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("priorities", Priority.values());
        return "issue/add-issue";
    }

    @PostMapping("update/{id}")
    public String updateIssue(@PathVariable("id") Long id, BindingResult result) {
        Issue issue = issueRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid issue id: " + id));
        if(result.hasErrors()) {
            issue.setId(id);
            return "issue/add-issue";
        }
        issueRepository.save(issue);
        return "redirect:/issues";
    }

    @GetMapping("/{id}")
    public String showIssueDetails(@ModelAttribute @PathVariable("id") Long id, Model model) {
        Issue issue = issueRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid issue id: " + id));
        model.addAttribute("issues", issueRepository.findAll());
        model.addAttribute("persons", personRepository.findAll());
        model.addAttribute("projects", projectRepository.findAll());
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("types", Type.values());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("issue", issue);
        model.addAttribute("creator", issue.getCreator());

        return "issue/details-issue";
    }

    @GetMapping("/delete/{id}")
    public String deleteIssue(@PathVariable("id") Long id) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid issue id : " + id));
        issueRepository.delete(issue);
        String email = issue.getCreator().getEmail();
        if (!email.isEmpty()){
            String subject = "Zamknięto Twoje zadanie";
            mailService.send(new Mail(email, subject, issueService.initMailContent(issue)));
        }
        return "redirect:/issues";
    }

}

