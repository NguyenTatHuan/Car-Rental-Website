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
@Document(indexName = "user_information")
public class UserInformationDocument {

    @Id
    private UUID id;

    private UUID userId;

    private UUID userInformationId;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String fullName;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String citizenID;

    private String birthday;

    private String gender;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String email;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String phone;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String address;

    private String nationality;

}
