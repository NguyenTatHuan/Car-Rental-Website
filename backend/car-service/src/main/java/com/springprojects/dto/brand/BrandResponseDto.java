package com.springprojects.dto.brand;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BrandResponseDto {

    private UUID id;

    private String name;

    private String country;

    private LocalDateTime createdAt;

}
