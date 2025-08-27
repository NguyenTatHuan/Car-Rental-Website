package com.springprojects.service.elasticsearch.location;

import com.springprojects.elasticsearch.LocationDocument;

import java.util.List;

public interface LocationSearchService {

    List<LocationDocument> searchByKeyword(String keyword);

}
