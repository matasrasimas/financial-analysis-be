package org.example.converter;

import org.example.model.boundary.BoundaryUserLoginDTO;
import org.example.model.domain.UserLoginDTO;

public class UserLoginB2DConverter extends Converter<BoundaryUserLoginDTO, UserLoginDTO> {
    @Override
    protected UserLoginDTO convert(BoundaryUserLoginDTO input) {
        return new UserLoginDTO(
                input.email(),
                input.password()
        );
    }
}
