package br.com.donus.bankbackend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.donus.bankbackend.dto.AccountDTO;
import br.com.donus.bankbackend.dto.DepositDTO;
import br.com.donus.bankbackend.dto.TransferDTO;
import br.com.donus.bankbackend.exceptions.AccountNotFoundException;
import br.com.donus.bankbackend.exceptions.PersonAccountFoundException;
import br.com.donus.bankbackend.exceptions.TransferBalanceNegativeException;
import br.com.donus.bankbackend.model.Account;
import br.com.donus.bankbackend.repository.AccountRepository;
import br.com.donus.bankbackend.util.Constants;
import br.com.donus.bankbackend.util.model.AccountModel;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {

	private static final Integer ACCOUNT_ID = 1;

	@Autowired
	private AccountService accountService;
	
	@MockBean
	private AccountRepository accountRepository;
	
	@Test
    public void findByIdWhenAccountNotFoundException() throws Exception {
		given(accountRepository.findById(anyInt())).willReturn(Optional.empty());
		
        Throwable thrown = catchThrowable(() -> accountService.findById(1));

        //@formatter:off
        assertThat(thrown).isInstanceOf(AccountNotFoundException.class)
                            .hasMessageContaining("Conta bancária não encontrada.");
        //@formatter:on
        
        verify(accountRepository).findById(anyInt());
        verifyNoMoreInteractions(accountRepository);
    }
	
	@Test
    public void findByIdReturnAccount() throws Exception {
		Account account = AccountModel.getAccount();
		given(accountRepository.findById(anyInt())).willReturn(Optional.of(account));
		
		Account accountDB = accountService.findById(ACCOUNT_ID);

        assertThat(accountDB.getId()).isEqualTo(ACCOUNT_ID);
        assertThat(accountDB.getDocument()).isEqualTo(account.getDocument());
        assertThat(accountDB.getName()).isEqualTo(account.getName());
        assertThat(accountDB.getBalance()).isEqualTo(account.getBalance());
        
        verify(accountRepository).findById(anyInt());
        verifyNoMoreInteractions(accountRepository);
    }
	
	
	@Test
    public void createAccountWhenPersonAccountFoundException() throws Exception {
		Account account = AccountModel.getAccount();
		given(accountRepository.findByDocument(anyString())).willReturn(Optional.of(account));
		
		Throwable thrown = catchThrowable(() -> accountService.create(AccountModel.getAccountDTOSaveController()));

		//@formatter:off
        assertThat(thrown).isInstanceOf(PersonAccountFoundException.class)
                            .hasMessageContaining("Pessoa já possui conta bancária cadastrada.");
        
        //@formatter:on
        
        verify(accountRepository).findByDocument(anyString());
        verifyNoMoreInteractions(accountRepository);
    }
	
	@Test
    public void createAccount() throws Exception {
		given(accountRepository.findByDocument(anyString())).willReturn(Optional.empty());
		
		doAnswer(invocation -> {
			Account modified = (Account) invocation.getArguments()[0];
            modified.setId(ACCOUNT_ID);
            return modified;
        }).when(accountRepository).save(any(Account.class));

		AccountDTO accountDTO = AccountModel.getAccountDTOSaveController();
		Account accountSave = accountService.create(accountDTO);

		assertThat(accountSave.getId()).isEqualTo(ACCOUNT_ID);
		assertThat(accountSave.getName()).isEqualTo(accountDTO.getName());
		assertThat(accountSave.getDocument()).isEqualTo(accountDTO.getDocument());
		assertThat(accountSave.getBalance()).isEqualTo(Constants.BALANCE_INITIAL);
		 
        verify(accountRepository).findByDocument(anyString());
        verify(accountRepository).save(any(Account.class));
        verifyNoMoreInteractions(accountRepository);
    }

	@Test
    public void depositAccountWhenAccountNotFoundException() throws Exception {
		given(accountRepository.findById(anyInt())).willReturn(Optional.empty());
		
		Throwable thrown = catchThrowable(() -> accountService.deposit(AccountModel.getDepositDTO()));

		//@formatter:off
		 assertThat(thrown).isInstanceOf(AccountNotFoundException.class)
         					.hasMessageContaining("Conta bancária não encontrada.");
        //@formatter:on
        
        verify(accountRepository).findById(anyInt());
        verifyNoMoreInteractions(accountRepository);
    }
	
	@Test
    public void depositAccount() throws Exception {
		Account account = AccountModel.getAccount();
		given(accountRepository.findById(anyInt())).willReturn(Optional.of(account));
		
		doAnswer(invocation -> {
			return (Account) invocation.getArguments()[0];
        }).when(accountRepository).save(any(Account.class));
		
		DepositDTO depositDTO = AccountModel.getDepositDTO();
		Account accountUpdate = accountService.deposit(depositDTO);
		
		assertThat(accountUpdate.getId()).isEqualTo(account.getId());
		assertThat(account.getBalance()).isEqualTo(depositDTO.getValue());

        verify(accountRepository).findById(anyInt());
        verify(accountRepository).save(any(Account.class));
        verifyNoMoreInteractions(accountRepository);
    }
	
	@Test
    public void transferAccountBalanceNegativeException() throws Exception {
		Account originAccount = AccountModel.getOriginAccount();
		given(accountRepository.findById(2)).willReturn(Optional.of(originAccount));

		Account destinyAccount = AccountModel.getAccount();
		given(accountRepository.findById(1)).willReturn(Optional.of(destinyAccount));
		
		doAnswer(invocation -> {
			return (Account) invocation.getArguments()[0];
        }).when(accountRepository).save(any(Account.class));
		
		TransferDTO transferDTO = new TransferDTO();
		transferDTO.setOriginAccountId(1);
		transferDTO.setDestinyAccountId(2);
		transferDTO.setValue(100.0);
		
		Throwable thrown = catchThrowable(() -> accountService.transfer(transferDTO));

		//@formatter:off
        assertThat(thrown).isInstanceOf(TransferBalanceNegativeException.class)
                            .hasMessageContaining("Saldo insuficiente para realizar a transferência.");
        
        //@formatter:on
        verify(accountRepository, times(2)).findById(anyInt());
        verifyNoMoreInteractions(accountRepository);
    }
	
															
}
