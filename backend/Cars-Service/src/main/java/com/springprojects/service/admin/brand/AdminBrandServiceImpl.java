package com.springprojects.service.admin.brand;

import com.springprojects.dto.brand.BrandCreateDto;
import com.springprojects.dto.brand.BrandResponseDto;
import com.springprojects.dto.brand.BrandUpdateDto;
import com.springprojects.entity.Brand;
import com.springprojects.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminBrandServiceImpl implements AdminBrandService {

    private final BrandRepository brandRepository;

    private BrandResponseDto mapToResponseDto(Brand brand) {
        BrandResponseDto dto = new BrandResponseDto();
        dto.setId(brand.getId());
        dto.setName(brand.getName());
        dto.setCountry(brand.getCountry());
        dto.setCreatedAt(brand.getCreatedAt());
        return dto;
    }

    @Override
    public BrandResponseDto createBrand(BrandCreateDto brandCreateDto) {
        Brand brand = new Brand();
        brand.setName(brandCreateDto.getName());
        brand.setCountry(brandCreateDto.getCountry());
        Brand savedBrand = brandRepository.save(brand);
        return mapToResponseDto(savedBrand);
    }

    @Override
    public BrandResponseDto updateBrand(UUID id, BrandUpdateDto brandUpdateDto) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found brand with id: " + id));

        if (brandUpdateDto.getName() != null) {
            brand.setName(brandUpdateDto.getName());
        }
        if (brandUpdateDto.getCountry() != null) {
            brand.setCountry(brandUpdateDto.getCountry());
        }

        Brand updatedBrand = brandRepository.save(brand);
        return mapToResponseDto(updatedBrand);
    }

    @Override
    public void deleteBrand(UUID id) {
        if (!brandRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found brand with id: " + id);
        }
        brandRepository.deleteById(id);
    }

    @Override
    public BrandResponseDto getBrandById(UUID id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found brand with id: " + id));
        return mapToResponseDto(brand);
    }

    @Override
    public List<BrandResponseDto> getAllBrands() {
        return brandRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
}
