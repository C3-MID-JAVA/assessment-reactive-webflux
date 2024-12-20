package com.reactivo.banco.mapper;

import com.reactivo.banco.model.dto.ClientInDTO;
import com.reactivo.banco.model.dto.ClientOutDTO;
import com.reactivo.banco.model.entity.Client;

public class ClientMapper {

    public static Client toEntity(ClientInDTO clientInDTO) {
        if (clientInDTO == null) {
            return null;
        }

        Client client = new Client();
        client.setIdentification(clientInDTO.getIdentification());
        client.setFirstName(clientInDTO.getFirstName());
        client.setLastName(clientInDTO.getLastName());
        client.setEmail(clientInDTO.getEmail());
        client.setPhone(clientInDTO.getPhone());
        client.setAddress(clientInDTO.getAddress());
        client.setBirthDate(clientInDTO.getBirthDate());

        return client;
    }

    public static ClientOutDTO toDTO(Client client) {
        if (client == null) {
            return null;
        }

        ClientOutDTO clientOutDTO = new ClientOutDTO();
        clientOutDTO.setId(client.getId());
        clientOutDTO.setIdentification(client.getIdentification());
        clientOutDTO.setFirstName(client.getFirstName());
        clientOutDTO.setLastName(client.getLastName());
        clientOutDTO.setEmail(client.getEmail());
        clientOutDTO.setPhone(client.getPhone());
        clientOutDTO.setAddress(client.getAddress());
        clientOutDTO.setBirthDate(client.getBirthDate());

        return clientOutDTO;
    }

}
