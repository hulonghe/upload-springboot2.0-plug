package com.hlh.util.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义了一个Password注解，
 * 而且该注解上标注了@Constraint注解，使用@Constraint注解标注表明我们定义了一个用于限制的注解。
 * -@Constraint注解的validatedBy属性用于指定我们定义的当前限制类型需要被哪个ConstraintValidator进行校验。 在上面代码中我们指定了Money限制类型的校验类是MoneyValidator。
 * 另外需要注意的是我们在定义自己的限制类型的注解时有三个属性是必须定义的，
 * 如上面代码所示的message、groups和payload属性。
 */

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface PasswordBase {

    int min() default 6;

    int max() default 50;

    String message() default "密码不符合要求";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
