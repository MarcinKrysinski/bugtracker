package pl.krysinski.bugtracker.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(PersonService personService, MarkdownParserUtils markdownParserUtils, ProjectRepository projectRepository) {
        this.personService = personService;
        this.markdownParserUtils = markdownParserUtils;
        this.projectRepository = projectRepository;
    }


    void addCreatorToProject(Project project, Principal principal) {
        Optional<Person> loggedUser = personService.getLoggedUser(principal);
        loggedUser.ifPresent(project::setCreator);
    }

     void markdownParser(Project project) {
        project.setHtml(markdownParserUtils.markdownToHTML(project.getDescription()));
    }

    @Cacheable(value = "projects")
    public Page<Project> findAll(ProjectFilter projectFilter, Pageable pageable){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return projectRepository.findAll(projectFilter.buildQuery(), pageable);
    }

    @CacheEvict(value = "projects", allEntries = true)
    public void save(Project project) {
        projectRepository.save(project);
    }

    @CacheEvict(value = "projects", allEntries = true)
    public void delete(Project project) {
        projectRepository.delete(project);
    }
}
