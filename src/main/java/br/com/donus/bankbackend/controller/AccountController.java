package br.com.donus.bankbackend.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.donus.bankbackend.dto.AccountDTO;
import br.com.donus.bankbackend.dto.DepositDTO;
import br.com.donus.bankbackend.dto.TransferDTO;
import br.com.donus.bankbackend.model.Account;
import br.com.donus.bankbackend.service.AccountService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/account", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

	@Autowired
	private AccountService accountService;
	
	@ApiOperation(value = "Consultar conta bancária")
	@GetMapping(path = "/{id}")
	public ResponseEntity<Account> findById(@PathVariable Integer id) {
	   Account account =  accountService.findById(id);
	   return ResponseEntity.ok().body(account);
	}
	
	@ApiOperation(value = "Criar conta bancária.")
	@PostMapping
	public ResponseEntity<Integer> create(@Valid @RequestBody AccountDTO accountDTO) {
		Account account =  accountService.create(accountDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(account.getId());
	}
	
	@ApiOperation(value = "Depositar na conta bancária.")
	@PostMapping(path = "/deposit")
	public ResponseEntity<Void> deposit(@Valid @RequestBody DepositDTO deposit) {
		accountService.deposit(deposit);
		return ResponseEntity.ok().build();
	}
	
	@ApiOperation(value = "Transferir para conta bancária.")
	@PostMapping(path = "/transfer")
	public ResponseEntity<Void> transfer(@Valid @RequestBody TransferDTO transfer) {
		accountService.transfer(transfer);
		return ResponseEntity.ok().build();
	}

}
