package com.springproject.repository;

import com.springproject.elasticsearch.UserDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserElasticsearchRepository extends ElasticsearchRepository<UserDocument, UUID> {

    List<UserDocument> findByUsernameContaining(String keyword);

}
