package ec.com.example.bank_account.mapper;

import ec.com.example.bank_account.dto.UserRequestDTO;
import ec.com.example.bank_account.dto.UserResponseDTO;
import ec.com.example.bank_account.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User mapToEntity(UserRequestDTO userRequestDTO) {
        if (userRequestDTO == null) {
            return null;
        }
        User user = new User();
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setCi(userRequestDTO.getCi());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());
        user.setStatus(userRequestDTO.getStatus());
        return user;
    }

    public UserResponseDTO mapToDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponseDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getCi(),
                user.getEmail(),
                user.getStatus()
        );
    }
}