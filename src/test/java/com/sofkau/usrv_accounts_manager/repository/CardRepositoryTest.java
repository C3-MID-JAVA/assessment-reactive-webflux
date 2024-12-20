package com.sofkau.usrv_accounts_manager.repository;

import com.sofkau.usrv_accounts_manager.model.AccountModel;
import com.sofkau.usrv_accounts_manager.model.CardModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

@DataMongoTest
class CardRepositoryTest {
    @Autowired
    private CardRepository cardRepository;

    @BeforeEach
    void setUp() {
        cardRepository.deleteAll().block();
    }

    @AfterEach
    void tearDown() {
        cardRepository.deleteAll().block();
    }

    @Test
    @DisplayName("Should return FALSE when CardCvv does not exist")
    void existsByCardCVV() {
        CardModel card = new CardModel();
        card.setCardNumber("123456789");

        StepVerifier.create( cardRepository.save(card))
                .expectNext(card)
                .verifyComplete();
        StepVerifier.create(cardRepository.existsByCardCVV("1234"))
                .expectNext(false)
                .verifyComplete();

    }
    @Test
    @DisplayName("Should return TRUE when CardCvv already exist")
    void existsByCardCVV_Existing() {
        CardModel card = new CardModel();
        card.setCardNumber("123456789");
        card.setCardCVV("1234");

        StepVerifier.create( cardRepository.save(card))
                .expectNext(card)
                .verifyComplete();
        StepVerifier.create(cardRepository.existsByCardCVV("1234"))
                .expectNext(true)
                .verifyComplete();

    }

    @Test
    @DisplayName("Should found a Card on the database with the same card number")
    void findByCardNumber() {
        CardModel card = new CardModel();
        card.setCardNumber("123456789");

        StepVerifier.create( cardRepository.save(card))
                .expectNext(card)
                .verifyComplete();

        StepVerifier.create(cardRepository.findByCardNumber(card.getCardNumber()))
                .expectNextMatches(cc->cc.getCardNumber().equals(card.getCardNumber()))
                .verifyComplete();

    }

    @Test
    @DisplayName("Should found all the Cards on the database with the same account id")
    void findByAccount_Id() {
        AccountModel account = new AccountModel();
        account.setAccountNumber("123456789");
        account.setId("6762467aee327545bebedebb");
        CardModel card = new CardModel();
        CardModel card2 = new CardModel();
        CardModel card3 = new CardModel();
        card.setCardNumber("123456789");
        card2.setCardNumber("987654321");
        card3.setCardNumber("147258369");
        card.setAccount(account);
        card2.setAccount(account);
        card3.setAccount(account);
        StepVerifier.create(cardRepository.save(card))
                .expectNext(card)
                .verifyComplete();
        StepVerifier.create(cardRepository.save(card2))
                .expectNext(card2)
                .verifyComplete();
        StepVerifier.create(cardRepository.save(card3))
                .expectNext(card3)
                .verifyComplete();

        StepVerifier.create(cardRepository.findByAccount_Id(account.getId()))
                .expectNextMatches(cc->cc.getCardNumber().equals(card.getCardNumber()))
                .expectNextMatches(cc->cc.getCardNumber().equals(card2.getCardNumber()))
                .expectNextMatches(cc->cc.getCardNumber().equals(card3.getCardNumber()))
                .verifyComplete();

    }
}