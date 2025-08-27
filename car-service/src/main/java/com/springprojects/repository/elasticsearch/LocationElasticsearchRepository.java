package com.springprojects.repository.elasticsearch;

import com.springprojects.elasticsearch.LocationDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LocationElasticsearchRepository extends ElasticsearchRepository<LocationDocument, UUID> {

    List<LocationDocument> findByNameContaining(String keyword);

    List<LocationDocument> findByProvinceContaining(String keyword);

    List<LocationDocument> findByDistrictContaining(String keyword);

    List<LocationDocument> findByAddressContaining(String keyword);

}
