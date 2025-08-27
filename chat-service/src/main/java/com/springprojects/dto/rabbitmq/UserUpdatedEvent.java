package com.springprojects.dto.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdatedEvent {

    private UUID userId;

    private String newUsername;

    private String newFullName;

}
