package pl.krysinski.bugtracker.project;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.krysinski.bugtracker.issue.Issue;
import pl.krysinski.bugtracker.issue.IssueRepository;
import pl.krysinski.bugtracker.person.PersonRepository;
import pl.krysinski.bugtracker.security.SecurityService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Controller
@Slf4j
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final ProjectService projectService;
    private final PersonRepository personRepository;
    private final IssueRepository issueRepository;
    private final SecurityService securityService;

    @Autowired
    public ProjectController(ProjectRepository projectRepository, ProjectService projectService, PersonRepository personRepository, IssueRepository issueRepository, SecurityService securityService) {
        this.projectRepository = projectRepository;
        this.projectService = projectService;
        this.personRepository = personRepository;
        this.issueRepository = issueRepository;
        this.securityService = securityService;
    }

    @GetMapping()
    public String projects(@ModelAttribute ProjectFilter projectFilter, Model model) throws InterruptedException {
        model.addAttribute("projects", projectService.findAll(projectFilter) );
        model.addAttribute("creator", personRepository.findAll());
        model.addAttribute("filter", projectFilter);
        log.debug("Getting project list: {}", model);
        return "project/projects";
    }

    @GetMapping("/create")
    @Secured("ROLE_MANAGE_PROJECTS")
    public String showProjectForm(Model model) {
        model.addAttribute("project", new Project());
        log.debug("Getting project create form: {}", model);
        return "project/add-project";
    }

    @PostMapping("/save")
    public String saveProject(@Valid Project project, BindingResult result, Principal principal){
        String usernameLoggedPerson = securityService.getUsernameLoggedUser();

        if (result.hasErrors()){
            log.error("There was a problem. The project: " + project + " was not saved.");
            log.error("Error: {}", result);
            log.debug("BindingResult: {}", result);
            return "project/add-project";
        }
        projectService.addCreatorToProject(project, principal);
        projectService.markdownParser(project);
        projectService.save(project);


        log.info("Created new project: " + project.getName() + " by: " + usernameLoggedPerson);
        log.debug("Create new project: {}", project);
        return "redirect:/projects";
    }



    @GetMapping("edit/{id}")
    @Secured("ROLE_MANAGE_PROJECTS")
    public String showUpdateForm(@PathVariable("id") Long id, Model model){
        Project project =  projectRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid project id: " + id));
        model.addAttribute("project", project);
        log.debug("Getting project update form: {}", model);
        log.debug("Getting project to update: {}", project);
        return "project/add-project";
    }

    @PostMapping("update/{id}")
    public String updateProject(@PathVariable("id") Long id, BindingResult result, @Valid Project project){
        String usernameLoggedPerson = securityService.getUsernameLoggedUser();
        if (result.hasErrors()){
            project.setId(id);
            log.error("There was a problem. The project: " + project + " was not updated.");
            log.error("Error: {}", result);
            log.debug("BindingResult: {}", result);
            return "project/add-project";
        }
        projectService.save(project);

        log.info("Updated project: " + project.getName() + " by: " + usernameLoggedPerson);
        log.debug("Updated project: {}", project);
        return "redirect:/projects";
    }



    @GetMapping("/{id}")
    public String showProjectDetails(@ModelAttribute @PathVariable("id") Long id, Model model) {
        Project project = projectRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid project id: " + id));
        model.addAttribute("creator", project.getCreator());
        model.addAttribute("project", project);
        log.debug("Getting project update form: {}", model);
        log.debug("Getting details project: {}", project);
        return "project/details-project";
    }

    @GetMapping("/delete/{id}")
    @Secured("ROLE_MANAGE_PROJECTS")
    public String deleteProject(@PathVariable("id") Long id) {
        Project project = projectRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid project id: " + id));
        List<Issue> allIssuesByProject = issueRepository.findAllByProject(project);
        String usernameLoggedPerson = securityService.getUsernameLoggedUser();

        log.info("Deleted " + project + " by " + usernameLoggedPerson);
        log.debug("Deleting issues connected with project: {}", allIssuesByProject);
        log.debug("Deleted project: {}", project);

        issueRepository.deleteAll(allIssuesByProject);
        projectService.delete(project);
        return "redirect:/projects";
    }

}
