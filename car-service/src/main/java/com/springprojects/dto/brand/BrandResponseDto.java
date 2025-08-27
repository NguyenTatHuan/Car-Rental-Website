package com.springprojects.dto.brand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandResponseDto {

    private UUID id;

    private String name;

    private String country;

    private LocalDateTime createdAt;

}
