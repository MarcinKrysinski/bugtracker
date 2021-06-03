package pl.krysinski.bugtracker.validators;

import org.springframework.beans.factory.annotation.Autowired;
import pl.krysinski.bugtracker.person.Person;
import pl.krysinski.bugtracker.project.Project;
import pl.krysinski.bugtracker.project.ProjectRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class ProjectNameUniquenessValidator implements ConstraintValidator<UniqueProjectName, Project> {
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectNameUniquenessValidator(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    @Override
    public void initialize(UniqueProjectName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Project project, ConstraintValidatorContext ctx) {
        Optional<Project> foundProject = projectRepository.findByName(project.getName());

        if (foundProject.isEmpty()) {
            return true;
        }

        boolean projectNameIsUnique = project.getId() != null && foundProject.get().getId().equals(project.getId());

        if (!projectNameIsUnique) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("name")
                    .addConstraintViolation();
        }

        return projectNameIsUnique;
    }
}
