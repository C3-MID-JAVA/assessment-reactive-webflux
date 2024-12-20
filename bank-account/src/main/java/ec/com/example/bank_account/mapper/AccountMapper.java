package ec.com.example.bank_account.mapper;

import ec.com.example.bank_account.dto.AccountRequestDTO;
import ec.com.example.bank_account.dto.AccountResponseDTO;
import ec.com.example.bank_account.entity.Account;
import ec.com.example.bank_account.repository.TypeAccountRepository;
import ec.com.example.bank_account.repository.UserRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AccountMapper {
    private final UserMapper userMapper;
    private final TypeAccountMapper typeAccountMapper;

    public AccountMapper(UserMapper userMapper, TypeAccountMapper typeAccountMapper) {
        this.userMapper = userMapper;
        this.typeAccountMapper = typeAccountMapper;
    }

    public Account mapToEntity(AccountRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Account account = new Account();
        account.setNumber(dto.getNumber());
        account.setAvailableBalance(dto.getAvailableBalance());
        account.setRetainedBalance(dto.getRetainedBalance());
        account.setStatus(dto.getStatus());
        return account;
    }

    public AccountResponseDTO mapToDTO(Account account) {
        if (account == null) {
            return null;
        }

        return new AccountResponseDTO(
                account.getNumber(),
                account.getAvailableBalance(),
                account.getRetainedBalance(),
                account.getStatus(),
                account.getUser() != null ? Mono.just(userMapper.mapToDTO(account.getUser())) : null,
                account.getTypeAccount() != null ? Mono.just(typeAccountMapper.mapToDTO(account.getTypeAccount())) : null
        );
    }
}