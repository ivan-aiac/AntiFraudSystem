package aiac.antifraudsystem.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CreditCardValidator implements ConstraintValidator<CreditCard, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank() || value.length() != 16) {
            return false;
        }
        int luhn = 0;
        for (int i = 1; i <= value.length(); i++) {
            int num = value.charAt(i - 1) - '0';
            if (i % 2 != 0) {
                num *= 2;
                if (num > 9) {
                    num -= 9;
                }
            }
            luhn += num;
        }
        return luhn % 10 == 0;
    }
}
