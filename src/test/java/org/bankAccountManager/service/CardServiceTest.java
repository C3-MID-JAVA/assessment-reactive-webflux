package org.bankAccountManager.service.implementations;

import org.bankAccountManager.entity.Card;
import org.bankAccountManager.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CardServiceTest {

    /*@Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardServiceImplementation cardService;

    private Card card;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        card = new Card(1, "1111-2222-3333-4444", "debit", "2025-12-31", "123", 1);
    }

    @Test
    void testCreateCard_success() {
        // Arrange
        when(cardRepository.existsById(card.getId())).thenReturn(false);
        when(cardRepository.save(any(Card.class))).thenReturn(card);

        // Act
        Card createdCard = cardService.createCard(card);

        // Assert
        assertNotNull(createdCard);
        assertEquals(card.getId(), createdCard.getId());
        assertEquals(card.getCardNumber(), createdCard.getCardNumber());
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    void testCreateCard_cardExists_throwsException() {
        // Arrange
        when(cardRepository.existsById(card.getId())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cardService.createCard(card);
        });

        assertEquals("Card already exists", exception.getMessage());
        verify(cardRepository, never()).save(card);
    }

    @Test
    void testGetCardById_success() {
        // Arrange
        when(cardRepository.findCardById(card.getId())).thenReturn(card);

        // Act
        Card foundCard = cardService.getCardById(card.getId());

        // Assert
        assertNotNull(foundCard);
        assertEquals(card.getId(), foundCard.getId());
        verify(cardRepository, times(1)).findCardById(card.getId());
    }

    @Test
    void testGetCardById_notFound() {
        // Arrange
        when(cardRepository.findCardById(card.getId())).thenReturn(null);

        // Act
        Card foundCard = cardService.getCardById(card.getId());

        // Assert
        assertNull(foundCard);
        verify(cardRepository, times(1)).findCardById(card.getId());
    }

    @Test
    void testGetCardByNumber_success() {
        // Arrange
        when(cardRepository.findCardByCardNumber(card.getCardNumber())).thenReturn(card);

        // Act
        Card foundCard = cardService.getCardByNumber(card.getCardNumber());

        // Assert
        assertNotNull(foundCard);
        assertEquals(card.getCardNumber(), foundCard.getCardNumber());
        verify(cardRepository, times(1)).findCardByCardNumber(card.getCardNumber());
    }

    @Test
    void testGetCardsByAccountId_success() {
        // Arrange
        when(cardRepository.findCardsByAccountId(card.getAccountId())).thenReturn(List.of(card));

        // Act
        List<Card> cards = cardService.getCardsByAccountId(card.getAccountId());

        // Assert
        assertNotNull(cards);
        assertFalse(cards.isEmpty());
        verify(cardRepository, times(1)).findCardsByAccountId(card.getAccountId());
    }

    @Test
    void testGetCardsByType_success() {
        // Arrange
        when(cardRepository.findCardsByCardType(card.getCardType())).thenReturn(List.of(card));

        // Act
        List<Card> cards = cardService.getCardsByType(card.getCardType());

        // Assert
        assertNotNull(cards);
        assertFalse(cards.isEmpty());
        verify(cardRepository, times(1)).findCardsByCardType(card.getCardType());
    }

    @Test
    void testUpdateCard() {
        // Arrange
        when(cardRepository.save(card)).thenReturn(card);

        // Act
        Card updatedCard = cardService.updateCard(card);

        // Assert
        assertNotNull(updatedCard);
        assertEquals(card.getId(), updatedCard.getId());
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    void testDeleteCard() {
        // Act
        cardService.deleteCard(card);

        // Assert
        verify(cardRepository, times(1)).delete(card);
    }*/
}
