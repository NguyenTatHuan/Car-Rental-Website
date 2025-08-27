package com.springprojects.service.elasticsearch.brand;

import com.springprojects.elasticsearch.BrandDocument;

import java.util.List;

public interface BrandSearchService {

    List<BrandDocument> searchByKeyword(String keyword);

}
