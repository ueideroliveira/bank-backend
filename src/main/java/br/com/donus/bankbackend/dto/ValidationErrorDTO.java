package br.com.donus.bankbackend.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ValidationErrorDTO {
    private int value;
    private String reasonPhrase;
    private List<FieldErrorDTO> fieldErrors;

    public ValidationErrorDTO(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
        this.fieldErrors = new ArrayList<>();
    }

    public void addFieldError(String path, String message, String defaultMessage) {
        FieldErrorDTO error = new FieldErrorDTO(path, message, defaultMessage);
        fieldErrors.add(error);
    }

    public void addFieldError(String path, String message, Object invalidValue) {
        FieldErrorDTO error = new FieldErrorDTO(path, message, invalidValue);
        fieldErrors.add(error);
    }
    
    public static ValidationErrorDTO getValidationErrorDTO(HttpStatus httpStatus) {
        return new ValidationErrorDTO(httpStatus.value(), httpStatus.getReasonPhrase());
    }
}
