package org.example.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RestOrgUnit(@JsonProperty("id") String id,
                          @JsonProperty(required = true, value = "orgId") String orgId,
                          @JsonProperty(required = true, value = "title") String title,
                          @JsonProperty("code") String code,
                          @JsonProperty("address") String address) {
}
