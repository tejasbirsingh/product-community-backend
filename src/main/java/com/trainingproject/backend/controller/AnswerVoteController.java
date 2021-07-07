package com.trainingproject.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trainingproject.backend.dto.AnswerVoteDto;
import com.trainingproject.backend.service.AnswerVoteService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/answer-votes/")
@AllArgsConstructor
public class AnswerVoteController {

	private AnswerVoteService answerVoteService;

	@PostMapping
	public ResponseEntity<Void> vote(@RequestBody AnswerVoteDto answerVoteDto) {

		answerVoteService.vote(answerVoteDto);

		return new ResponseEntity<>(HttpStatus.OK);
	}
}