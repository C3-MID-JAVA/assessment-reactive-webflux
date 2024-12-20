package org.bankAccountManager.repository;

import org.bankAccountManager.entity.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class CardRepositoryTest {

    /*@Autowired
    private CardRepository cardRepository;

    private Card card1;
    private Card card2;
    private Card card3;

    @BeforeEach
    public void setUp() {
        // Limpia la base de datos antes de cada prueba
        cardRepository.deleteAll();

        // Inicializa las tarjetas de prueba
        card1 = new Card();
        card1.setId(1);
        card1.setCardNumber("1111-2222-3333-4444");
        card1.setCardType("debit");
        card1.setExpirationDate("2025-12-31");
        card1.setCvv("123");

        card2 = new Card();
        card2.setId(2);
        card2.setCardNumber("5555-6666-7777-8888");
        card2.setCardType("credit");
        card2.setExpirationDate("2026-06-30");
        card2.setCvv("456");

        card3 = new Card();
        card3.setId(3);
        card3.setCardNumber("9999-0000-1111-2222");
        card3.setCardType("debit");
        card3.setExpirationDate("2027-09-15");
        card3.setCvv("789");

        cardRepository.saveAll(Arrays.asList(card1, card2, card3));
    }

    @Test
    public void testFindCardById_Positive() {
        // Prueba positiva: Encontrar una tarjeta por su ID
        Card foundCard = cardRepository.findCardById(1);
        assertThat(foundCard).isNotNull();
        assertThat(foundCard.getCardNumber()).isEqualTo("1111-2222-3333-4444");
    }

    @Test
    public void testFindCardById_Negative() {
        // Prueba negativa: Buscar una tarjeta inexistente
        Card foundCard = cardRepository.findCardById(999);
        assertThat(foundCard).isNull();
    }

    @Test
    public void testExistsById_Positive() {
        // Prueba positiva: Verificar si existe una tarjeta con ID 1
        Boolean exists = cardRepository.existsById(1);
        assertThat(exists).isTrue();
    }

    @Test
    public void testExistsById_Negative() {
        // Prueba negativa: Verificar si existe una tarjeta con un ID inexistente
        Boolean exists = cardRepository.existsById(999);
        assertThat(exists).isFalse();
    }

    @Test
    public void testFindCardByCardNumber_Positive() {
        // Prueba positiva: Encontrar una tarjeta por su número
        Card foundCard = cardRepository.findCardByCardNumber("5555-6666-7777-8888");
        assertThat(foundCard).isNotNull();
        assertThat(foundCard.getCardType()).isEqualTo("credit");
    }

    @Test
    public void testFindCardByCardNumber_Negative() {
        // Prueba negativa: Buscar una tarjeta con un número inexistente
        Card foundCard = cardRepository.findCardByCardNumber("0000-1111-2222-3333");
        assertThat(foundCard).isNull();
    }

    @Test
    public void testFindAll_Positive() {
        // Prueba positiva: Obtener todas las tarjetas
        List<Card> cards = cardRepository.findAll();
        assertThat(cards).isNotEmpty();
        assertThat(cards.size()).isEqualTo(3);
    }

    @Test
    public void testFindAll_Negative() {
        // Prueba negativa: Verificar cuando no hay tarjetas
        cardRepository.deleteAll();
        List<Card> cards = cardRepository.findAll();
        assertThat(cards).isEmpty();
    }

    @Test
    public void testFindCardsByCardType_Positive() {
        // Prueba positiva: Encontrar tarjetas por su tipo
        List<Card> debitCards = cardRepository.findCardsByCardType("debit");
        assertThat(debitCards).isNotEmpty();
        assertThat(debitCards.size()).isEqualTo(2);
        assertThat(debitCards).extracting(Card::getCardNumber)
                .contains("1111-2222-3333-4444", "9999-0000-1111-2222");
    }

    @Test
    public void testFindCardsByCardType_Negative() {
        // Prueba negativa: Buscar tarjetas con un tipo inexistente
        List<Card> platinumCards = cardRepository.findCardsByCardType("platinum");
        assertThat(platinumCards).isEmpty();
    }

    @Test
    public void testSaveCard_Positive() {
        // Prueba positiva: Guardar una nueva tarjeta
        Card newCard = new Card();
        newCard.setId(4);
        newCard.setCardNumber("1234-5678-9012-3456");
        newCard.setCardType("debit");
        newCard.setExpirationDate("2028-03-01");
        newCard.setCvv("321");

        Card savedCard = cardRepository.save(newCard);
        assertThat(savedCard).isNotNull();
        assertThat(savedCard.getId()).isEqualTo(4);
        assertThat(savedCard.getCardNumber()).isEqualTo("1234-5678-9012-3456");
    }

    @Test
    public void testSaveCard_Negative() {
        // Prueba negativa: Intentar guardar una tarjeta con campos faltantes
        Card invalidCard = new Card();
        invalidCard.setId(5);
        invalidCard.setCardNumber(null); // Número de tarjeta faltante

        Card savedCard = cardRepository.save(invalidCard);
        assertThat(savedCard.getCardNumber()).isNull(); // Verifica que el campo faltante sea nulo
    }*/
}
