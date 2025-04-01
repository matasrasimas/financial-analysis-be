package org.example.converter;

import org.example.model.boundary.BoundaryUserDTO;
import org.example.model.domain.UserDTO;

public class UserDTOD2BConverter extends Converter<UserDTO, BoundaryUserDTO> {
    @Override
    protected BoundaryUserDTO convert(UserDTO input) {
        return new BoundaryUserDTO(
                input.id(),
                input.firstName(),
                input.lastName(),
                input.phoneNumber(),
                input.email()
        );
    }
}
