package com.springprojects.elasticsearch;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "conversations")
public class ConversationDocument {

    @Id
    private UUID id;

    private UUID customerId;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String username;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String fullName;

}
