package br.com.donus.bankbackend.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class AccountDTO {

	@NotBlank
	private String name;
	@Size(min = 11, max = 11)
	@NotBlank
	private String document;
}
