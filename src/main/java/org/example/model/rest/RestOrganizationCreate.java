package org.example.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RestOrganizationCreate(@JsonProperty(required = true, value = "title") String title,
                                     @JsonProperty("code") String code,
                                     @JsonProperty("address") String address) {
}
