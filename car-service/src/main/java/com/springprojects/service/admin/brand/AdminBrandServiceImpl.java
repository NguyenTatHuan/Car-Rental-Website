package com.springprojects.service.admin.brand;

import com.springprojects.dto.brand.BrandCreateDto;
import com.springprojects.dto.brand.BrandResponseDto;
import com.springprojects.dto.brand.BrandUpdateDto;
import com.springprojects.elasticsearch.BrandDocument;
import com.springprojects.entity.Brand;
import com.springprojects.repository.elasticsearch.BrandElasticsearchRepository;
import com.springprojects.repository.persistence.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminBrandServiceImpl implements AdminBrandService {

    private final BrandRepository brandRepository;

    private final BrandElasticsearchRepository brandElasticsearchRepository;

    private BrandResponseDto mapToResponseDto(Brand brand) {
        return BrandResponseDto.builder()
                .id(brand.getId())
                .name(brand.getName())
                .country(brand.getCountry())
                .createdAt(brand.getCreatedAt())
                .build();
    }

    private BrandDocument mapToDocument(Brand brand) {
        return BrandDocument.builder()
                .id(brand.getId())
                .name(brand.getName())
                .country(brand.getCountry())
                .build();
    }

    @Override
    @Transactional
    public BrandResponseDto createBrand(BrandCreateDto brandCreateDto) {
        if (brandRepository.existsByName(brandCreateDto.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Brand name already exists!");
        }

        Brand brand = Brand.builder()
                .name(brandCreateDto.getName())
                .country(brandCreateDto.getCountry())
                .build();

        Brand savedBrand = brandRepository.save(brand);
        brandElasticsearchRepository.save(mapToDocument(savedBrand));

        return mapToResponseDto(savedBrand);
    }

    @Override
    @Transactional
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
        brandElasticsearchRepository.save(mapToDocument(updatedBrand));

        return mapToResponseDto(updatedBrand);
    }

    @Override
    @Transactional
    public void deleteBrand(UUID id) {
        if (!brandRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found brand with id: " + id);
        }

        brandRepository.deleteById(id);
        brandElasticsearchRepository.deleteById(id);
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
