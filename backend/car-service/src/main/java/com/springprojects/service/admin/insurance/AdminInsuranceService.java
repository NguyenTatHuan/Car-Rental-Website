package com.springprojects.service.admin.insurance;

import com.springprojects.dto.insurance.InsuranceCreateDto;
import com.springprojects.dto.insurance.InsuranceResponseDto;
import com.springprojects.dto.insurance.InsuranceUpdateDto;

import java.util.List;
import java.util.UUID;

public interface AdminInsuranceService {

    InsuranceResponseDto createInsurance(InsuranceCreateDto insuranceCreateDto);

    InsuranceResponseDto updateInsurance(UUID id, InsuranceUpdateDto insuranceUpdateDto);

    void deleteInsurance(UUID id);

    InsuranceResponseDto getInsuranceById(UUID id);

    List<InsuranceResponseDto> getInsuranceByCarId(UUID carId);

    List<InsuranceResponseDto> getAllInsurances();

}
