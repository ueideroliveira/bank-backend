package br.com.donus.bankbackend.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FieldErrorDTO {
    private String field;
    private String message;
    private String defaultMessage;
    private Object invalidValue;

    public FieldErrorDTO(String field, String message, String defaultMessage) {
        this.field = field;
        this.message = message;
        this.defaultMessage = defaultMessage;
    }

    public FieldErrorDTO(String field, String message, Object invalidValue) {
        this.field = field;
        this.message = message;
        this.invalidValue = invalidValue;
    }
}

