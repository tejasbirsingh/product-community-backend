package com.trainingproject.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {

	private Long id;
	private Long questionId;
	private String text;
	private String userName;

}
