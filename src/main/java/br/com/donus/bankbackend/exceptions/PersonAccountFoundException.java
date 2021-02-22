package br.com.donus.bankbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PersonAccountFoundException extends RuntimeException {

	private static final long serialVersionUID = -4936647921370211115L;

	public PersonAccountFoundException(String message) {
        super(message);
    }
}
