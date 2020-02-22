package com.hlh.util.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * 密码校验类
 * 限制类型校验类必须实现接口javax.validation.ConstraintValidator，
 * 并实现它的initialize和isValid方法。
 */

public class PasswordValidator implements ConstraintValidator<PasswordBase, String> {

    private int min;
    private int max;
    private String reg;
    private Pattern pattern;

    public void initialize(PasswordBase password) {
        // TODO Auto-generated method stub
        this.min = password.min();
        this.max = password.max();

        reg = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d$@$!%*#?&]{" + min + "," + max + "}$";
        pattern = Pattern.compile(reg);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return false;
        }

        if (value.length() < this.min || value.length() > max) {
            return false;
        }

        return pattern.matcher(value).matches();
    }
}
