package com.springprojects.service.elasticsearch.carType;

import com.springprojects.elasticsearch.CarTypeDocument;

import java.util.List;

public interface CarTypeSearchService {

    List<CarTypeDocument> searchByKeyword(String keyword);

}
