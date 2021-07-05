package com.trainingproject.backend.controller;

import static org.springframework.http.ResponseEntity.status;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trainingproject.backend.dto.QuestionRequest;
import com.trainingproject.backend.dto.QuestionResponse;
import com.trainingproject.backend.model.Question;
import com.trainingproject.backend.service.QuestionService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/questions/")
@AllArgsConstructor
public class QuestionController {

	private QuestionService questionService;

	@PostMapping
	public ResponseEntity<Void> createQuestion(@RequestBody QuestionRequest questionRequest) {
		questionService.save(questionRequest);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<QuestionResponse>> getAllQuestions() {
		return status(HttpStatus.OK).body(questionService.getAllQuestions());
	}
	

	@GetMapping("/{id}")
	public ResponseEntity<QuestionResponse> getQuestion(@PathVariable Long id) {
		return status(HttpStatus.OK).body(questionService.getQuestion(id));
	}

	@GetMapping("by-category/{id}")
	public ResponseEntity<List<QuestionResponse>> getQuestionsByCategory(@PathVariable Long id) {
		return status(HttpStatus.OK).body(questionService.getQuestionsByCategory(id));
	}
																																			
	@GetMapping("by-user/{name}")
	public ResponseEntity<List<QuestionResponse>> getQuestionsByUsername(@PathVariable String name) {
		return status(HttpStatus.OK).body(questionService.getQuestionsByUsername(name));
	}
	
	@GetMapping("close/{id}/{answerId}")
	public ResponseEntity<Void> closeQuestion(@PathVariable Long id, @PathVariable Long answerId) {
	    	questionService.close(id,answerId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	@GetMapping("/closed")
	public ResponseEntity<Integer> getTotalClosedQues() {
		return status(HttpStatus.OK).body(questionService.getNumberOfClosedQuestions());
	}

	@GetMapping("by-category-name/{name}")
	public ResponseEntity<List<QuestionResponse>> getQuestionsByCategoryName(@PathVariable String name) {
		return status(HttpStatus.OK).body(questionService.getQuestionsByCategoryName(name));
	}
	@GetMapping("by-productid/{id}")
	public ResponseEntity<List<QuestionResponse>> getQuestionsProductId(@PathVariable String id) {
		return status(HttpStatus.OK).body(questionService.getQuestionsByProductId(id));
	}
	
	@PutMapping("/update")
	public ResponseEntity<Void> updateQuestion(@RequestBody Question question) {
		questionService.update(question);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}


	
}
