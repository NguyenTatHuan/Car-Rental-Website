package com.springproject.service.elasticsearch.user;

import com.springproject.elasticsearch.UserDocument;
import com.springproject.repository.UserElasticsearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserSearchServiceImpl implements UserSearchService {

    private final UserElasticsearchRepository userElasticsearchRepository;

    @Override
    public List<UserDocument> searchByKeyword(String keyword) {
        return userElasticsearchRepository.findByUsernameContaining(keyword);
    }

}
