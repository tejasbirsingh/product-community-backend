package com.trainingproject.backend.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trainingproject.backend.dto.CategoryDto;
import com.trainingproject.backend.exceptions.ProductWebsiteException;
import com.trainingproject.backend.mapper.CategoryMapper;
import com.trainingproject.backend.model.Category;
import com.trainingproject.backend.repository.CategoryRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor

public class CategoryService {

	private final CategoryRepository categoryRepository;
	private final CategoryMapper categoryMapper;

	@Transactional
	public CategoryDto save(CategoryDto categoryDto) {
		Category save = categoryRepository.save(categoryMapper.mapDtoToCategory(categoryDto));
		categoryDto.setId(save.getId());
		return categoryDto;
	}

	@Transactional(readOnly = true)
	public List<CategoryDto> getAll() {
		return categoryRepository.findAll().stream().map(categoryMapper::mapCategoryToDto).collect(toList());
	}

	public CategoryDto getCategory(Long id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new ProductWebsiteException("No category found with ID - " + id));
		return categoryMapper.mapCategoryToDto(category);
	}
}
