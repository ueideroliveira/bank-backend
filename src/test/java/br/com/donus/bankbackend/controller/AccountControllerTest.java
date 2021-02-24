package br.com.donus.bankbackend.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.donus.bankbackend.dto.AccountDTO;
import br.com.donus.bankbackend.dto.DepositDTO;
import br.com.donus.bankbackend.dto.TransferDTO;
import br.com.donus.bankbackend.model.Account;
import br.com.donus.bankbackend.service.AccountService;
import br.com.donus.bankbackend.util.model.AccountModel;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class AccountControllerTest {

	private static final String PATH_ACCOUNT = "/account";
	private static final String PATH_ACCOUNT_DEPOSIT = PATH_ACCOUNT + "/deposit";
	private static final String PATH_ACCOUNT_TRANSFER = PATH_ACCOUNT + "/transfer";
	private static final Integer ACCOUNT_ID = 1;
	
	private static final String MUST_NOT_BE_NULL = "must not be null";
	private static final String MUST_NOT_BE_BLANK = "must not be blank";
	private static final String MUST_BE_GREATER_ZERO = "must be greater than 0";
	private static final String MUST_BE_LESS_THAN_OR_EQUAL_TO_LIMIT = "must be less than or equal to 2000";

	private static final String FIELD_NAME = "accountDTO.name";
	private static final String FIELD_DOCUMENT = "accountDTO.document";
	private static final String FIELD_ACCOUNT_ID = "depositDTO.accountId";
	private static final String FIELD_VALUE = "depositDTO.value";
	
	private static final String FIELD_TRANSFER_ORIGIN_ACCOUNT_ID = "transferDTO.originAccountId";
	private static final String FIELD_TRANSFER_DESTINY_ACCOUNT_ID = "transferDTO.destinyAccountId";
	private static final String FIELD_TRANSFER_VALUE = "transferDTO.value";

	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
	private AccountService accountService;
	
	protected byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

	@Test
    public void getAccountWhenRequestingAccountId() throws Exception {
        
		Account response = AccountModel.getAccount();
        
        given(this.accountService.findById(anyInt())).willReturn(response);

        //@formatter:off
        this.mockMvc.perform(
                            get(PATH_ACCOUNT + "/" + ACCOUNT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .with(csrf())
                   ).andDo(print())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", equalTo(ACCOUNT_ID)))
                    .andExpect(jsonPath("$.name", equalTo(response.getName())))
                    .andExpect(jsonPath("$.document", equalTo(response.getDocument())))
                    .andExpect(jsonPath("$.balance", equalTo(response.getBalance())));
      //@formatter:on
    }

	
	@Test
    public void createAccountWhenPostingReturnId() throws Exception {
		AccountDTO dto = AccountModel.getAccountDTOSaveController();

        doAnswer(invocation -> {
            return AccountModel.getAccount();
        }).when(this.accountService).create(dto);
        
        //@formatter:off
        this.mockMvc.perform(
                            post(PATH_ACCOUNT) 
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(convertObjectToJsonBytes(dto))
                            .with(csrf())
                   ).andDo(print())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(content().string("1"));
                    ;
        //@formatter:on
    }
	
	@Test
    public void createAccountWithParametersEmpty() throws Exception {
		AccountDTO dto = new AccountDTO();

        //@formatter:off
        this.mockMvc.perform(
                            post(PATH_ACCOUNT) 
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(convertObjectToJsonBytes(dto))
                            .with(csrf())
                   ).andDo(print())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.value", equalTo(HttpStatus.BAD_REQUEST.value())))
                    .andExpect(jsonPath("$.reasonPhrase", equalTo(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                    .andExpect(jsonPath("$.fieldErrors", hasSize(2)) )
                    .andExpect(jsonPath("$.fieldErrors[*].field").value(containsInAnyOrder(FIELD_NAME, FIELD_DOCUMENT)))
                    .andExpect(jsonPath("$.fieldErrors[*].message").value(containsInAnyOrder(MUST_NOT_BE_BLANK, MUST_NOT_BE_BLANK)))
                    .andExpect(jsonPath("$.fieldErrors[*].defaultMessage").value(containsInAnyOrder(MUST_NOT_BE_BLANK, MUST_NOT_BE_BLANK)));
        //@formatter:on
    }
	
	@Test
    public void depositAccountWithParametersEmpty() throws Exception {
		DepositDTO dto = new DepositDTO();

        //@formatter:off
        this.mockMvc.perform(
                            post(PATH_ACCOUNT_DEPOSIT) 
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(convertObjectToJsonBytes(dto))
                            .with(csrf())
                   ).andDo(print())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.value", equalTo(HttpStatus.BAD_REQUEST.value())))
                    .andExpect(jsonPath("$.reasonPhrase", equalTo(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                    .andExpect(jsonPath("$.fieldErrors", hasSize(2)) )
                    .andExpect(jsonPath("$.fieldErrors[*].field").value(containsInAnyOrder(FIELD_VALUE, FIELD_ACCOUNT_ID)))
                    .andExpect(jsonPath("$.fieldErrors[*].message").value(containsInAnyOrder(MUST_NOT_BE_NULL, MUST_NOT_BE_NULL)))
                    .andExpect(jsonPath("$.fieldErrors[*].defaultMessage").value(containsInAnyOrder(MUST_NOT_BE_NULL, MUST_NOT_BE_NULL)));
        //@formatter:on
    }
	
	@Test
    public void depositAccountWithValueNegative() throws Exception {
		DepositDTO dto = new DepositDTO();
		dto.setAccountId(1);
		dto.setValue(-1.0);
        //@formatter:off
        this.mockMvc.perform(
                            post(PATH_ACCOUNT_DEPOSIT) 
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(convertObjectToJsonBytes(dto))
                            .with(csrf())
                   ).andDo(print())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.value", equalTo(HttpStatus.BAD_REQUEST.value())))
                    .andExpect(jsonPath("$.reasonPhrase", equalTo(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                    .andExpect(jsonPath("$.fieldErrors", hasSize(1)) )
                    .andExpect(jsonPath("$.fieldErrors[0].field", equalTo(FIELD_VALUE)))
                    .andExpect(jsonPath("$.fieldErrors[0].message", equalTo(MUST_BE_GREATER_ZERO)))
                    .andExpect(jsonPath("$.fieldErrors[0].defaultMessage", equalTo(MUST_BE_GREATER_ZERO)));
        //@formatter:on
    }
	
	@Test
    public void depositAccountWithValueGreaterLimit() throws Exception {
		DepositDTO dto = new DepositDTO();
		dto.setAccountId(1);
		dto.setValue(2500.0);
        //@formatter:off
        this.mockMvc.perform(
                            post(PATH_ACCOUNT_DEPOSIT) 
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(convertObjectToJsonBytes(dto))
                            .with(csrf())
                   ).andDo(print())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.value", equalTo(HttpStatus.BAD_REQUEST.value())))
                    .andExpect(jsonPath("$.reasonPhrase", equalTo(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                    .andExpect(jsonPath("$.fieldErrors", hasSize(1)) )
                    .andExpect(jsonPath("$.fieldErrors[0].field", equalTo(FIELD_VALUE)))
                    .andExpect(jsonPath("$.fieldErrors[0].message", equalTo(MUST_BE_LESS_THAN_OR_EQUAL_TO_LIMIT)))
                    .andExpect(jsonPath("$.fieldErrors[0].defaultMessage", equalTo(MUST_BE_LESS_THAN_OR_EQUAL_TO_LIMIT)));
        //@formatter:on
    }
	
	@Test
    public void depositAccount() throws Exception {
		DepositDTO dto = new DepositDTO();
		dto.setAccountId(1);
		dto.setValue(2000.0);
		
        //@formatter:off
        this.mockMvc.perform(
                            post(PATH_ACCOUNT_DEPOSIT) 
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(convertObjectToJsonBytes(dto))
                            .with(csrf())
                   ).andDo(print())
                    .andExpect(status().isOk());
        //@formatter:on
    }
	
	@Test
    public void transferAccount() throws Exception {
		TransferDTO dto = new TransferDTO();
		dto.setDestinyAccountId(2);
		dto.setOriginAccountId(1);
		dto.setValue(100.0);
		
        //@formatter:off
        this.mockMvc.perform(
                            post(PATH_ACCOUNT_TRANSFER) 
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(convertObjectToJsonBytes(dto))
                            .with(csrf())
                   ).andDo(print())
                    .andExpect(status().isOk());
        //@formatter:on
    }
	
	@Test
    public void transferAccountWithParametersEmpty() throws Exception {
		TransferDTO dto = new TransferDTO();

        //@formatter:off
        this.mockMvc.perform(
                            post(PATH_ACCOUNT_TRANSFER) 
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(convertObjectToJsonBytes(dto))
                            .with(csrf())
                   ).andDo(print())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.value", equalTo(HttpStatus.BAD_REQUEST.value())))
                    .andExpect(jsonPath("$.reasonPhrase", equalTo(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                    .andExpect(jsonPath("$.fieldErrors", hasSize(3)))
                    .andExpect(jsonPath("$.fieldErrors[*].field").value(containsInAnyOrder(FIELD_TRANSFER_ORIGIN_ACCOUNT_ID, FIELD_TRANSFER_DESTINY_ACCOUNT_ID, FIELD_TRANSFER_VALUE)))
                    .andExpect(jsonPath("$.fieldErrors[*].message").value(containsInAnyOrder(MUST_NOT_BE_NULL, MUST_NOT_BE_NULL, MUST_NOT_BE_NULL)))
                    .andExpect(jsonPath("$.fieldErrors[*].defaultMessage").value(containsInAnyOrder(MUST_NOT_BE_NULL, MUST_NOT_BE_NULL, MUST_NOT_BE_NULL)));
        //@formatter:on
    }

}
