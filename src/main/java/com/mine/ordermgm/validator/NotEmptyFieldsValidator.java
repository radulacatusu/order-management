package com.mine.ordermgm.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Objects;

/**
 * @stefanl
 */
public class NotEmptyFieldsValidator implements ConstraintValidator<NotEmptyLongFields, List<Long>> {
    @Override
    public void initialize(NotEmptyLongFields notEmptyFields) {
    }

    @Override
    public boolean isValid(List<Long> objects, ConstraintValidatorContext context) {
        if(Objects.nonNull(objects)) {
            return objects.stream().allMatch(nef -> nef != null);
        }
        return false;
    }
}
