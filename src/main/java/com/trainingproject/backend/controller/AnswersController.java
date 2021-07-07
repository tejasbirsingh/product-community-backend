package com.trainingproject.backend.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trainingproject.backend.dto.AnswerRequest;
import com.trainingproject.backend.dto.AnswerResponse;
import com.trainingproject.backend.service.AnswerService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/answers/")
@AllArgsConstructor
public class AnswersController {
	private AnswerService answerService;

	@PostMapping
	public ResponseEntity<Void> createAnswer(@RequestBody AnswerRequest answersDto) {
		answerService.save(answersDto);
		return new ResponseEntity<>(CREATED);
	}

	@GetMapping("/by-question/{questionId}")
	public ResponseEntity<List<AnswerResponse>> getAllAnswersForQuestion(@PathVariable Long questionId) {
		return ResponseEntity.status(OK).body(answerService.getAllAnswersForQuestion(questionId));
	}

	@GetMapping("/by-user/{userName}")
	public ResponseEntity<List<AnswerResponse>> getAllAnswersForUser(@PathVariable String userName) {
		return ResponseEntity.status(OK).body(answerService.getAllAnswersForUser(userName));
	}

	@GetMapping("/{answerId}")
	public ResponseEntity<AnswerResponse> getAnswer (@PathVariable Long answerId) {
		return ResponseEntity.status(OK).body(answerService.getAnswer(answerId));
	}
	@GetMapping("/accept/{answerId}")
	public ResponseEntity<Void> accept(@PathVariable Long answerId) {
	    	answerService.accept(answerId);
		return new ResponseEntity<>(OK);
	}
	

}
