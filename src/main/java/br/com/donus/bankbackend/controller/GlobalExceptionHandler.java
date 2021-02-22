package br.com.donus.bankbackend.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.donus.bankbackend.dto.ValidationErrorDTO;
import br.com.donus.bankbackend.exceptions.AccountNotFoundException;
import br.com.donus.bankbackend.exceptions.PersonAccountFoundException;
import br.com.donus.bankbackend.exceptions.TransferBalanceNegativeException;
import br.com.donus.bankbackend.util.Constants;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@Autowired
    private MessageSource messageSource;
	
	private Locale locale = LocaleContextHolder.getLocale();

	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleArgumentNotValidException(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        ValidationErrorDTO dto = ValidationErrorDTO.getValidationErrorDTO(HttpStatus.BAD_REQUEST);
        //@formatter:off
        result.getAllErrors()
                .stream()
                .forEach(
                        objectError -> {
                        	String field;
                        	if (FieldError.class.isInstance(objectError))
                        		field = objectError.getObjectName() + Constants.DOT + ((FieldError) objectError).getField();
                        	else
                        		field = objectError.getObjectName();
                        	dto.addFieldError(field , messageSource.getMessage(objectError, locale), objectError.getDefaultMessage());
                        }
                );
        //@formatter:on
        return ResponseEntity.badRequest().body(dto);
    }
	
	
	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<Object> handleAccountNotFoundException(AccountNotFoundException exception) {
		ValidationErrorDTO dto = ValidationErrorDTO.getValidationErrorDTO(HttpStatus.NOT_FOUND);
		dto.addFieldError(exception.getClass().getSimpleName(), exception.getMessage(), exception.getMessage());
		return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(PersonAccountFoundException.class)
	public ResponseEntity<Object> handlePersonAccountFoundException(PersonAccountFoundException exception) {
		ValidationErrorDTO dto = ValidationErrorDTO.getValidationErrorDTO(HttpStatus.BAD_REQUEST);
		dto.addFieldError(exception.getClass().getSimpleName(), exception.getMessage(), exception.getMessage());
		return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(TransferBalanceNegativeException.class)
	public ResponseEntity<Object> handlePersonAccountFoundException(TransferBalanceNegativeException exception) {
		ValidationErrorDTO dto = ValidationErrorDTO.getValidationErrorDTO(HttpStatus.BAD_REQUEST);
		dto.addFieldError(exception.getClass().getSimpleName(), exception.getMessage(), exception.getMessage());
		return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
	}

}
