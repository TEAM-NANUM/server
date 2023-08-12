package server.nanum.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import server.nanum.annotation.ImageFileType;

import java.util.Arrays;
import java.util.List;

public class ImageFileTypeValidator implements ConstraintValidator<ImageFileType, String> {
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/bmp",
            "image/webp",
            "image/jpg"
    );


    @Override
    public void initialize(ImageFileType constraintAnnotation) {
    }

    @Override
    public boolean isValid(String fileType, ConstraintValidatorContext context) {
        return fileType != null && ALLOWED_IMAGE_TYPES.contains(fileType);
    }
}
