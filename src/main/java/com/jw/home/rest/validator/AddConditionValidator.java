package com.jw.home.rest.validator;

import com.jw.home.common.spec.ConditionType;
import com.jw.home.rest.dto.AddScenarioReq;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

public class AddConditionValidator implements ConstraintValidator<AddConditionConstraint, AddScenarioReq.AddConditionDto> {
    private final Validator validator;

    public AddConditionValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    public boolean isValid(AddScenarioReq.AddConditionDto value, ConstraintValidatorContext context) {
        Class<?> group = getConstraintGroup(value.getType());
        Set<ConstraintViolation<AddScenarioReq.AddConditionDto>> constraintViolations = validator.validate(value, group);
        if (!constraintViolations.isEmpty()) {
            context.disableDefaultConstraintViolation();
            constraintViolations
                    .forEach(constraintViolation -> context.buildConstraintViolationWithTemplate(constraintViolation.getMessageTemplate())
                            .addPropertyNode(constraintViolation.getPropertyPath().toString())
                            .addConstraintViolation());
            return false;
        }
        return true;
    }

    private Class<?> getConstraintGroup(ConditionType conditionType) {
        switch (conditionType) {
            case week:
                return WeekCondition.class;
            case weekInterval:
                return WeekIntervalCondition.class;
            case device:
                return DeviceCondition.class;
        }
        return null;
    }
}
