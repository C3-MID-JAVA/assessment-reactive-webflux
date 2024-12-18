package com.reactivo.banco.mapper;

import com.reactivo.banco.model.dto.CardInDTO;
import com.reactivo.banco.model.dto.CardOutDTO;
import com.reactivo.banco.model.entity.Card;

public class CardMapper {

    public static Card toEntity(CardInDTO cardInDTO) {
        if (cardInDTO == null) {
            return null;
        }

        Card card = new Card();
        card.setCardNumber(cardInDTO.getCardNumber());
        card.setType(cardInDTO.getType());

        return card;
    }

    public static CardOutDTO toDTO(Card card) {
        if (card == null) {
            return null;
        }

        CardOutDTO cardOutDTO = new CardOutDTO();
        cardOutDTO.setId(card.getId());
        cardOutDTO.setCardNumber(card.getCardNumber());
        cardOutDTO.setType(card.getType());

        return cardOutDTO;
    }
}
