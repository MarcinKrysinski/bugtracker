package pl.krysinski.bugtracker.issue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.krysinski.bugtracker.enums.Priority;
import pl.krysinski.bugtracker.enums.Status;
import pl.krysinski.bugtracker.enums.Type;
import pl.krysinski.bugtracker.mail.MailService;
import pl.krysinski.bugtracker.person.PersonRepository;
import pl.krysinski.bugtracker.person.PersonService;
import pl.krysinski.bugtracker.project.ProjectRepository;
import pl.krysinski.bugtracker.security.SecurityService;
import pl.krysinski.bugtracker.utils.MarkdownParserUtils;

import javax.validation.Valid;
import java.security.Principal;


@Controller
@Slf4j
@RequestMapping("/issues")
public class IssueController {

    private final IssueRepository issueRepository;
    private final PersonRepository personRepository;
    private final ProjectRepository projectRepository;
    private final IssueService issueService;
    private final SecurityService securityService;


    @Autowired
    public IssueController(IssueRepository issueRepository, PersonRepository personRepository, ProjectRepository projectRepository, IssueService issueService, SecurityService securityService) {
        this.issueRepository = issueRepository;
        this.personRepository = personRepository;
        this.projectRepository = projectRepository;
        this.issueService = issueService;
        this.securityService = securityService;
    }


    @GetMapping
    public String issues(@ModelAttribute IssueFilter issueFilter, Model model, Pageable pageable){
        Page<Issue> issues = issueService.findAll(issueFilter, pageable);
        model.addAttribute("issues", issues);
        model.addAttribute("assignedPerson", personRepository.findAll());
        model.addAttribute("projects", projectRepository.findAll());
        model.addAttribute("filter", issueFilter);
        model.addAttribute("types", Type.values());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("priorities", Priority.values());
        log.debug("Getting issues list: {}", model);
        return "issue/issues";
    }


    @GetMapping("/create")
    public String showIssueForm(Model model) {
        model.addAttribute("persons", personRepository.findAllByEnabled(true));
        model.addAttribute("projects", projectRepository.findAll());
        model.addAttribute("types", Type.values());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("issue", new Issue());
        log.debug("Getting issue create form: {}", model);
        return "issue/add-issue";
    }

    @CacheEvict("issues")
    @PostMapping("/save")
    public String saveIssue(@Valid Issue issue, BindingResult result, Principal principal, Model model) {
        String usernameLoggedPerson = securityService.getUsernameLoggedUser();
        if (result.hasErrors()){
            model.addAttribute("persons", personRepository.findAllByEnabled(true));
            model.addAttribute("projects", projectRepository.findAll());
            model.addAttribute("types", Type.values());
            model.addAttribute("statuses", Status.values());
            model.addAttribute("priorities", Priority.values());
            log.error("There was a problem. The issue: " + issue + " was not saved.");
            log.error("Error: {}", result);
            log.debug("BindingResult: {}", result);
            log.debug("Model create new issue: {}", model);
            return "issue/add-issue";
        }

        issueService.addCreatorToIssue(issue, principal);
        issueService.markdownParser(issue);
        issueService.save(issue);

        log.info("Created new issue: " + issue.getName() + " by: " + usernameLoggedPerson);
        log.debug("Create new issue: {}", issue);
        log.debug("Model create new issue: {}", model);
        return "redirect:/issues";
    }

    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Issue issue = issueRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid issue id: " + id));
        model.addAttribute("issue", issue);
        model.addAttribute("persons", personRepository.findAllByEnabled(true));
        model.addAttribute("projects", projectRepository.findAll());
        model.addAttribute("types", Type.values());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("priorities", Priority.values());
        log.debug("Getting issue update form: {}", model);
        log.debug("Issue to update: {}", issue);
        return "issue/add-issue";
    }

    @PostMapping("update/{id}")
    public String updateIssue(@PathVariable("id") Long id, BindingResult result) {
        Issue issue = issueRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid issue id: " + id));
        String usernameLoggedPerson = securityService.getUsernameLoggedUser();

        if(result.hasErrors()) {
            issue.setId(id);
            log.error("There was a problem. The issue: " + issue + " was not update.");
            log.error("Error: {}", result);
            log.debug("BindingResult: {}", result);
            return "issue/add-issue";
        }
        issueService.save(issue);

        log.info("Updated issue: " + issue.getName() + " by: " + usernameLoggedPerson);
        log.debug("Updated issue: {}", issue);
        return "redirect:/issues";
    }

    @GetMapping("/{id}")
    public String showIssueDetails(@ModelAttribute @PathVariable("id") Long id, Model model) {
        Issue issue = issueRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cannot find issue of id: " + id));
        model.addAttribute("person", issue.getAssignee());
        model.addAttribute("project", issue.getProject());
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("types", Type.values());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("issue", issue);
        model.addAttribute("creator", issue.getCreator());
        log.debug("Getting issue details: {}", model);
        return "issue/details-issue";
    }

    @GetMapping("/delete/{id}")
    public String deleteIssue(@PathVariable("id") Long id) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid issue id : " + id));
        String emailAddress = issue.getCreator().getEmail();
        String usernameLoggedPerson = securityService.getUsernameLoggedUser();

        log.info("Deleted " + issue + " by " + usernameLoggedPerson);
        log.debug("Deleted issue: {}", issue);

        issueService.delete(issue);
        issueService.sendEmailAboutDeleteIssue(issue, emailAddress);
        return "redirect:/issues";
    }

}

