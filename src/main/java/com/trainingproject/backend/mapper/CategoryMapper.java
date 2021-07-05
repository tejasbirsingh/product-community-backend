package com.trainingproject.backend.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.trainingproject.backend.dto.CategoryDto;
import com.trainingproject.backend.model.Category;
import com.trainingproject.backend.model.Question;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

	@Mapping(target = "numberOfQuestions", expression = "java(mapQuestions(category.getQuestions()))")
	CategoryDto mapCategoryToDto(Category category);

	default Integer mapQuestions(List<Question> numberOfQuestions) {
		return numberOfQuestions.size();
	}

	@InheritInverseConfiguration
	@Mapping(target = "questions", ignore = true)
	Category mapDtoToCategory(CategoryDto categoryDto);
}
