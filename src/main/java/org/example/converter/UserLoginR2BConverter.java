package org.example.converter;

import org.example.model.boundary.BoundaryUserLoginDTO;
import org.example.model.rest.RestUserLoginDTO;

public class UserLoginR2BConverter extends Converter<RestUserLoginDTO, BoundaryUserLoginDTO> {
    @Override
    protected BoundaryUserLoginDTO convert(RestUserLoginDTO input) {
        return new BoundaryUserLoginDTO(
                input.email(),
                input.password()
        );
    }
}
