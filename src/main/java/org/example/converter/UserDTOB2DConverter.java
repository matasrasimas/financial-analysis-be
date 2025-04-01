package org.example.converter;

import org.example.model.boundary.BoundaryUserDTO;
import org.example.model.domain.UserDTO;

public class UserDTOB2DConverter extends Converter<BoundaryUserDTO, UserDTO> {
    @Override
    protected UserDTO convert(BoundaryUserDTO input) {
        return new UserDTO(
                input.id(),
                input.firstName(),
                input.lastName(),
                input.phoneNumber(),
                input.email()
        );
    }
}
