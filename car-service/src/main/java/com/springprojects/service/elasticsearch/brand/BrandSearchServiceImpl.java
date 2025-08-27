package com.springprojects.service.elasticsearch.brand;

import com.springprojects.elasticsearch.BrandDocument;
import com.springprojects.repository.elasticsearch.BrandElasticsearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BrandSearchServiceImpl implements BrandSearchService {

    private final BrandElasticsearchRepository brandElasticsearchRepository;

    @Override
    public List<BrandDocument> searchByKeyword(String keyword) {
        Set<BrandDocument> result = new HashSet<>();

        result.addAll(brandElasticsearchRepository.findByNameContaining(keyword));
        result.addAll(brandElasticsearchRepository.findByCountryContaining(keyword));

        return new ArrayList<>(result);
    }

}
