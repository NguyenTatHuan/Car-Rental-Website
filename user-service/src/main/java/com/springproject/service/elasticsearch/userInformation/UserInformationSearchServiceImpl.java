package com.springproject.service.elasticsearch.userInformation;

import com.springproject.elasticsearch.UserInformationDocument;
import com.springproject.repository.UserInformationElasticsearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserInformationSearchServiceImpl implements UserInformationSearchService {

    private final UserInformationElasticsearchRepository userInformationElasticsearchRepository;

    @Override
    public List<UserInformationDocument> searchByKeyword(String keyword) {
        Set<UserInformationDocument> result = new HashSet<>();

        result.addAll(userInformationElasticsearchRepository.findByFullNameContaining(keyword));
        result.addAll(userInformationElasticsearchRepository.findByCitizenIDContaining(keyword));
        result.addAll(userInformationElasticsearchRepository.findByEmailContaining(keyword));
        result.addAll(userInformationElasticsearchRepository.findByPhoneContaining(keyword));
        result.addAll(userInformationElasticsearchRepository.findByAddressContaining(keyword));

        return new ArrayList<>(result);
    }

}
