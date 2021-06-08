package pl.krysinski.bugtracker.validators;

import pl.krysinski.bugtracker.issue.Issue;
import pl.krysinski.bugtracker.project.Project;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ProjectMandatoryTitleValidator implements ConstraintValidator<ProjectMandatoryTitle, Project> {
    @Override
    public void initialize(ProjectMandatoryTitle constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Project project, ConstraintValidatorContext ctx) {
        if (project.getName().isEmpty()) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("name")
                    .addConstraintViolation();
            return false;
        } else {
            return true;
        }
    }
}
