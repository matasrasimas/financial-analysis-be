package org.example.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RestInvitation(@JsonProperty("id") String id,
                             @JsonProperty("senderId") String senderId,
                             @JsonProperty("receiverId") String receiverId,
                             @JsonProperty("organizationId") String organizationId,
                             @JsonProperty("createdAt") String createdAt,
                             @JsonProperty("isAccepted") boolean isAccepted) {
}
