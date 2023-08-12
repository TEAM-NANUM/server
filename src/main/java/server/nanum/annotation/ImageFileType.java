package server.nanum.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import server.nanum.utils.ImageFileTypeValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ImageFileTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageFileType {
    String message() default "유효한 이미지 타입이 아닙니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
