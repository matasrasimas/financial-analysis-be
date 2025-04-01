package org.example.converter;

import org.example.model.boundary.BoundaryUserDTO;
import org.example.model.rest.RestUserDTO;

import java.util.Optional;
import java.util.UUID;

public class UserDTOR2BConverter extends Converter<RestUserDTO, BoundaryUserDTO> {
    @Override
    protected BoundaryUserDTO convert(RestUserDTO input) {
        return new BoundaryUserDTO(
                UUID.fromString(input.id()),
                input.firstName(),
                input.lastName(),
                Optional.ofNullable(input.phoneNumber()),
                Optional.ofNullable(input.email())
        );
    }
}
