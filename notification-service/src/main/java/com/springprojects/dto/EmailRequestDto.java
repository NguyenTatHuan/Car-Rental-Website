package com.springprojects.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequestDto {

    private String to;

    private String subject;

    private String templateName;

    private Map<String, Object> variables;

}
