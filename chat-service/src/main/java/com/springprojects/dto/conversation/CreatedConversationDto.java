package com.springprojects.dto.conversation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatedConversationDto {

    @NotNull(message = "Customer ID must not be null")
    private UUID customerId;

    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotBlank(message = "Full name must not be blank")
    private String fullName;

}
