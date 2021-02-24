package br.com.donus.bankbackend.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data
public class TransferDTO {

	@NotNull
	private Integer originAccountId;
	@NotNull
	private Integer destinyAccountId;
	@NotNull
	@Positive
	private Double value;
}
