package com.hlh.util.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * 手机号校验类
 * 限制类型校验类必须实现接口javax.validation.ConstraintValidator，
 * 并实现它的initialize和isValid方法。
 */

public class PhoneValidator implements ConstraintValidator<PhoneBase, String> {

    private int phoneLength = 11;
    private String reg = "^0?1[3456789]\\d{9}$";
    private Pattern pattern = Pattern.compile(reg);

    public void initialize(PhoneBase money) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.length() != phoneLength) {
            return false;
        }
        return pattern.matcher(value).matches();
    }
}
