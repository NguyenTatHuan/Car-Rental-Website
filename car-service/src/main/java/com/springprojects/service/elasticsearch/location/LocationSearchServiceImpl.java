package com.springprojects.service.elasticsearch.location;

import com.springprojects.elasticsearch.LocationDocument;
import com.springprojects.repository.elasticsearch.LocationElasticsearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LocationSearchServiceImpl implements LocationSearchService {

    private final LocationElasticsearchRepository repository;

    @Override
    public List<LocationDocument> searchByKeyword(String keyword) {
        Set<LocationDocument> result = new HashSet<>();

        result.addAll(repository.findByNameContaining(keyword));
        result.addAll(repository.findByProvinceContaining(keyword));
        result.addAll(repository.findByDistrictContaining(keyword));
        result.addAll(repository.findByAddressContaining(keyword));

        return new ArrayList<>(result);
    }

}
