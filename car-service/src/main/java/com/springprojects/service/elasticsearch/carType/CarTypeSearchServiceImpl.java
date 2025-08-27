package com.springprojects.service.elasticsearch.carType;

import com.springprojects.elasticsearch.CarTypeDocument;
import com.springprojects.repository.elasticsearch.CarTypeElasticsearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarTypeSearchServiceImpl implements CarTypeSearchService {

    private final CarTypeElasticsearchRepository carTypeElasticsearchRepository;

    @Override
    public List<CarTypeDocument> searchByKeyword(String keyword) {
        return carTypeElasticsearchRepository.findByNameContaining(keyword);
    }

}
