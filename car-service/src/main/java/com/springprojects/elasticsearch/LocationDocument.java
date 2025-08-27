package com.springprojects.elasticsearch;

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
@Document(indexName = "location")
public class LocationDocument {

    @Id
    private UUID id;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String name;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String province;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String district;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String address;

}
