package com.springprojects.elasticsearch;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "insurance")
public class InsuranceDocument {

    @Id
    private UUID id;

    @Field(type = FieldType.Keyword)
    private UUID carId;

    @Field(type = FieldType.Keyword)
    private String licensePlate;

    @Field(type = FieldType.Text)
    private String carModel;

    @Field(type = FieldType.Text)
    private String provider;

    @Field(type = FieldType.Double)
    private double price;

    @Field(type = FieldType.Date, format = DateFormat.date)
    private String startDate;

    @Field(type = FieldType.Date, format = DateFormat.date)
    private String endDate;

}
