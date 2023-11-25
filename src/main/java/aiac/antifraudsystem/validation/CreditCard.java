package aiac.antifraudsystem.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CreditCardValidator.class)
@Documented
public @interface CreditCard {
    String message() default "Invalid Credit Card";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
