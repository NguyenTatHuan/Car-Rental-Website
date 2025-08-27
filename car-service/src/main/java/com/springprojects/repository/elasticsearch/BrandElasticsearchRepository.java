package com.springprojects.repository.elasticsearch;

import com.springprojects.elasticsearch.BrandDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BrandElasticsearchRepository extends ElasticsearchRepository<BrandDocument, UUID> {

    List<BrandDocument> findByCountryContaining(String keyword);

    List<BrandDocument> findByNameContaining(String keyword);

}
