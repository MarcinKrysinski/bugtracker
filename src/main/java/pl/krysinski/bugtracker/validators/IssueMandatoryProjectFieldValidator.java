package pl.krysinski.bugtracker.validators;

import pl.krysinski.bugtracker.issue.Issue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IssueMandatoryProjectFieldValidator implements ConstraintValidator<IssueMandatoryProjectField, Issue> {
    @Override
    public void initialize(IssueMandatoryProjectField constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Issue issue, ConstraintValidatorContext ctx) {
        if (issue.getProject() == null){
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("project")
                    .addConstraintViolation();
            return false;
        }else{
            return true;
        }

    }
}
