package com.springprojects.elasticsearch;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "car")
public class CarDocument {

    @Id
    private UUID id;

    @Field(type = FieldType.Keyword)
    private String licensePlate;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String brandName;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String model;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String carTypeName;

    @Field(type = FieldType.Keyword)
    private String fuelType;

    @Field(type = FieldType.Keyword)
    private String transmissionType;

    @Field(type = FieldType.Integer)
    private Integer year;

    @Field(type = FieldType.Integer)
    private Integer seats;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String color;

    @Field(type = FieldType.Double)
    private double pricePerDay;

    @Field(type = FieldType.Keyword)
    private String status;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String locationName;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String locationProvince;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String locationDistrict;

}
