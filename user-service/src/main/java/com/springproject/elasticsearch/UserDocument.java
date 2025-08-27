package com.springproject.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(indexName = "users")
public class UserDocument {

    @Id
    private UUID id;

    private UUID userId;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String username;

    private String role;

    private String status;

}
