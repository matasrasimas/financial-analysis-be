package org.example.converter;

import org.example.model.boundary.BoundaryUserCreate;
import org.example.model.rest.RestUserCreate;

import java.util.Optional;

public class UserCreateR2BConverter extends Converter<RestUserCreate, BoundaryUserCreate> {
    @Override
    protected BoundaryUserCreate convert(RestUserCreate input) {
        return new BoundaryUserCreate(
                input.firstName(),
                input.lastName(),
                Optional.ofNullable(input.phoneNumber()),
                Optional.ofNullable(input.email()),
                input.password()
        );
    }
}
