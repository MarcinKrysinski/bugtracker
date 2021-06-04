package pl.krysinski.bugtracker.validators;

import pl.krysinski.bugtracker.issue.Issue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IssueMandatoryAssigneeFieldValidator implements ConstraintValidator<IssueMandatoryAssigneeField, Issue> {
    @Override
    public void initialize(IssueMandatoryAssigneeField constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Issue issue, ConstraintValidatorContext ctx) {
        if (issue.getAssignee() == null){
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("assignee")
                    .addConstraintViolation();
            return false;
        }else{
            return true;
        }
    }
}
