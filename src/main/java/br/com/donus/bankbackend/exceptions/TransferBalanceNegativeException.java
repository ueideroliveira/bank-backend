package br.com.donus.bankbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TransferBalanceNegativeException extends RuntimeException {

	private static final long serialVersionUID = -7865102517321048346L;

	public TransferBalanceNegativeException(String message) {
        super(message);
    }
	
}
