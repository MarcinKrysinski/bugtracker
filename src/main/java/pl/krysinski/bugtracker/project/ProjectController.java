package pl.krysinski.bugtracker.project;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.krysinski.bugtracker.enums.Priority;
import pl.krysinski.bugtracker.enums.Status;
import pl.krysinski.bugtracker.enums.Type;
import pl.krysinski.bugtracker.issue.Issue;


@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping()
    public String projects(Model model){
        model.addAttribute("projects", projectRepository.findAll());
        return "project/projects";
    }

    @GetMapping("/create")
    public String showProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "project/add-project";
    }

    @PostMapping("/save")
    public String save(Project project, BindingResult result){
        if (result.hasErrors()){
            return "project/add-project";
        }
        projectRepository.save(project);
        return "redirect:/projects";
    }

    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model){
        Project project =  projectRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid project id: " + id));
        model.addAttribute("project", project);
        return "project/add-project";
    }

    @PostMapping("update/{id}")
    public String updateProject(@PathVariable("id") Long id, BindingResult result){
        Project project = projectRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid project id: " + id));
        if (result.hasErrors()){
            project.setId(id);
            return "project/add-project";
        }
        projectRepository.save(project);
        return "redirect:/projects";
    }

    @GetMapping("/{id}")
    public String showIssueDetails(@ModelAttribute @PathVariable("id") Long id, Model model) {
        Project project = projectRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid project id: " + id));
        model.addAttribute("project", project);
        return "project/details-project";
    }
}
