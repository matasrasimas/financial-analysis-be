package org.example.converter;

import org.example.model.boundary.BoundaryUserCreate;
import org.example.model.domain.UserCreate;

public class UserCreateB2DConverter extends Converter<BoundaryUserCreate, UserCreate> {
    @Override
    protected UserCreate convert(BoundaryUserCreate input) {
        return new UserCreate(
                input.firstName(),
                input.lastName(),
                input.phoneNumber(),
                input.email(),
                input.password()
        );
    }
}
