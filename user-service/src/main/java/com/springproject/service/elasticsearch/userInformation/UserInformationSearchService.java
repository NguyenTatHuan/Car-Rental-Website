package com.springproject.service.elasticsearch.userInformation;

import com.springproject.elasticsearch.UserInformationDocument;

import java.util.List;

public interface UserInformationSearchService {

    List<UserInformationDocument> searchByKeyword(String keyword);

}
