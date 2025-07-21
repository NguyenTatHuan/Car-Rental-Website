package com.springprojects.service;

import com.springprojects.dto.EmailRequestDto;

public interface EmailService {

    void sendTemplateEmail(EmailRequestDto dto);

}
