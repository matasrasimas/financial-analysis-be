package org.example.converter;

import org.example.model.boundary.BoundaryUserDTO;
import org.example.model.rest.RestUserDTO;

public class UserDTOB2RConverter extends Converter<BoundaryUserDTO, RestUserDTO> {
    @Override
    protected RestUserDTO convert(BoundaryUserDTO input) {
        return new RestUserDTO(
                input.id().toString(),
                input.firstName(),
                input.lastName(),
                input.phoneNumber().orElse(null),
                input.email().orElse(null)
        );
    }
}