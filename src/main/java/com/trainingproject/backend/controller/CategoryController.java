package com.trainingproject.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trainingproject.backend.dto.CategoryDto;
import com.trainingproject.backend.service.CategoryService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/category")
@AllArgsConstructor

public class CategoryController {

	private CategoryService categoryService;

	@PostMapping
	public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(categoryDto));
	}

	@GetMapping
	public ResponseEntity<List<CategoryDto>> getAllCategories() {
		return ResponseEntity.status(HttpStatus.OK).body(categoryService.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryDto> getCategory(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategory(id));
	}
}
