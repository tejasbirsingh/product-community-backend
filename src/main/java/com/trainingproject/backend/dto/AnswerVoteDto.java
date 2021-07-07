package com.trainingproject.backend.dto;

import com.trainingproject.backend.model.VoteType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerVoteDto {
	VoteType voteType;
	private Long answerId;
}