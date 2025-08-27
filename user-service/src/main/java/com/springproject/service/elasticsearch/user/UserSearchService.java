package com.springproject.service.elasticsearch.user;

import com.springproject.elasticsearch.UserDocument;

import java.util.List;

public interface UserSearchService {

    List<UserDocument> searchByKeyword(String keyword);

}
