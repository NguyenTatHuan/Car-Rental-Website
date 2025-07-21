package com.springprojects.dto.cartype;

import lombok.Data;

import java.util.UUID;

@Data
public class CarTypeResponseDto {

    private UUID id;

    private String name;

    private String description;

}
