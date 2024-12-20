package ec.com.example.bank_account.mapper;

import ec.com.example.bank_account.dto.CardRequestDTO;
import ec.com.example.bank_account.dto.CardResponseDTO;
import ec.com.example.bank_account.entity.Account;
import ec.com.example.bank_account.entity.Card;
import ec.com.example.bank_account.exception.RecordNotFoundException;
import ec.com.example.bank_account.repository.AccountRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CardMapper {
    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;

    public CardMapper(AccountMapper accountMapper, AccountRepository accountRepository) {
        this.accountMapper = accountMapper;
        this.accountRepository = accountRepository;
    }

    public Card mapToEntity(CardRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Account account = accountRepository.findById(dto.getAccountId())
                .switchIfEmpty(Mono.error(new RecordNotFoundException("Account not found."))).block();


        Card card = new Card();
        card.setHolderName(dto.getHolderName());
        card.setLimitation(dto.getLimitation());
        card.setCvcCode(dto.getCvcCode());
        card.setExpirationDate(dto.getExpirationDate());
        card.setStatus(dto.getStatus());
        card.setAccount(account);

        return card;
    }

    public CardResponseDTO mapToDTO(Card card) {
        if (card == null) {
            return null;
        }

        return new CardResponseDTO(
                card.getHolderName(),
                card.getLimitation(),
                card.getCvcCode(),
                card.getExpirationDate(),
                card.getStatus(),
                Mono.just(accountMapper.mapToDTO(card.getAccount())));
    }

}