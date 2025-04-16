package org.example.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RestLoginMetadata(@JsonProperty("token") String token,
                                @JsonProperty("activeOrgUnit")  String activeOrgUnit) {
}
