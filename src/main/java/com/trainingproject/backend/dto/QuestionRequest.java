package com.trainingproject.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionRequest {

	private Long questionId;
	private String categoryName;
	private String questionName;
	private String url;
	private String description;
	private String productId;
}
