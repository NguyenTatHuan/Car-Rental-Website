package com.springprojects.service.admin.brand;

import com.springprojects.dto.brand.BrandCreateDto;
import com.springprojects.dto.brand.BrandResponseDto;
import com.springprojects.dto.brand.BrandUpdateDto;

import java.util.List;
import java.util.UUID;

public interface AdminBrandService {

    BrandResponseDto createBrand(BrandCreateDto brandCreateDto);

    BrandResponseDto updateBrand(UUID id, BrandUpdateDto brandUpdateDto);

    void deleteBrand(UUID id);

    BrandResponseDto getBrandById(UUID id);

    List<BrandResponseDto> getAllBrands();

}
