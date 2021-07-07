package com.trainingproject.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerResponse {
	private Long id;
	private Long questionId;
	private String createdDate;
	private String text;
	private String userName;
	private Integer voteCount;
	private boolean upVote;
	private boolean downVote;
	private boolean accepted = false;
}