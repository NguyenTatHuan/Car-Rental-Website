package com.springprojects.service.customer.car;

import com.springprojects.dto.car.CarCustomerDto;

import java.util.List;
import java.util.UUID;

public interface CustomerCarService {

    List<CarCustomerDto> getAvailableCars();

    CarCustomerDto getCarDetail(UUID id);

}
