package com.springprojects.repository.elasticsearch;

import com.springprojects.elasticsearch.CarTypeDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CarTypeElasticsearchRepository extends ElasticsearchRepository<CarTypeDocument, UUID> {

    List<CarTypeDocument> findByNameContaining(String keyword);

}
