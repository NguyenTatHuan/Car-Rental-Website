package com.springproject.repository;

import com.springproject.elasticsearch.UserInformationDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.UUID;

public interface UserInformationElasticsearchRepository extends ElasticsearchRepository<UserInformationDocument, UUID> {

    List<UserInformationDocument> findByFullNameContaining(String keyword);

    List<UserInformationDocument> findByCitizenIDContaining(String keyword);

    List<UserInformationDocument> findByEmailContaining(String keyword);

    List<UserInformationDocument> findByPhoneContaining(String keyword);

    List<UserInformationDocument> findByAddressContaining(String keyword);

}
