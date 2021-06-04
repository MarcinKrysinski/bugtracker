package pl.krysinski.bugtracker.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IssueMandatoryAssigneeFieldValidator.class)
public @interface IssueMandatoryAssigneeField {
    String message() default "{assigneeField.mandatory.error}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
