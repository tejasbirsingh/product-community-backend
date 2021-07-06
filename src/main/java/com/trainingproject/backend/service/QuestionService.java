package com.trainingproject.backend.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trainingproject.backend.dto.QuestionRequest;
import com.trainingproject.backend.dto.QuestionResponse;
import com.trainingproject.backend.exceptions.CategoryNotFoundException;
import com.trainingproject.backend.exceptions.QuestionNotFoundException;
import com.trainingproject.backend.mapper.QuestionMapper;
import com.trainingproject.backend.model.Category;
import com.trainingproject.backend.model.Question;
import com.trainingproject.backend.model.User;
import com.trainingproject.backend.repository.CategoryRepository;
import com.trainingproject.backend.repository.QuestionRepository;
import com.trainingproject.backend.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class QuestionService {

	private final QuestionRepository questionRepository;
	private final CategoryRepository categoryRepository;
	private final UserRepository userRepository;
	private final AuthService authService;
	private final QuestionMapper questionMapper;

	public void save(QuestionRequest questionRequest) {
		Category category = categoryRepository.findByName(questionRequest.getCategoryName())
				.orElseThrow(() -> new CategoryNotFoundException(questionRequest.getCategoryName()));
		questionRepository.save(questionMapper.map(questionRequest, category, authService.getCurrentUser()));
	}
	
	public void update(QuestionResponse updatedQues) {
	    Question ques = questionRepository.findById(updatedQues.getId()).orElseThrow(()-> new QuestionNotFoundException(updatedQues.getId().toString()));
	    Category category = categoryRepository.findByName(updatedQues.getCategoryName())
			.orElseThrow(() -> new CategoryNotFoundException(updatedQues.getCategoryName()));
	    ques.setQuestionName(updatedQues.getQuestionName());
	    ques.setCategory(category);
	    ques.setDescription(updatedQues.getDescription());
	    ques.setUrl(updatedQues.getUrl());
	    ques.setProductId(updatedQues.getProductId());
	    ques.setCreatedDate(java.time.Instant.now());
	}
	
	public void close(Long id, Long answerId) {
		Question closedQues = questionRepository.findById(id).orElseThrow(()-> new QuestionNotFoundException(id.toString()));
		boolean isClosed = closedQues.isClosed();
		closedQues.setClosed(isClosed ? false : true);
		closedQues.setAnswerId(answerId);
		questionRepository.save(closedQues); 
	}

	@Transactional(readOnly = true)
	public QuestionResponse getQuestion(Long id) {
		Question question = questionRepository.findById(id).orElseThrow(() -> new QuestionNotFoundException(id.toString()));
		return questionMapper.mapToDto(question);
	}

	@Transactional(readOnly = true)
	public List<QuestionResponse> getAllQuestions() {
		return questionRepository.findAll().stream().map(questionMapper::mapToDto).collect(toList());
	}
	

	@Transactional(readOnly = true)
	public int getNumberOfClosedQuestions() {
		return questionRepository.findByClosed(true).size();
	}

	@Transactional(readOnly = true)
	public List<QuestionResponse> getQuestionsByCategory(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CategoryNotFoundException(categoryId.toString()));
		List<Question> questions = questionRepository.findAllByCategory(category);
		return questions.stream().map(questionMapper::mapToDto).collect(toList());
	}
	@Transactional(readOnly = true)
	public List<QuestionResponse> getQuestionsByCategoryName(String name) {
		Category category = categoryRepository.findByName(name)
				.orElseThrow(() -> new CategoryNotFoundException(name));
		List<Question> questions = questionRepository.findAllByCategory(category);
		return questions.stream().map(questionMapper::mapToDto).collect(toList());
	}
	

	@Transactional(readOnly = true)
	public List<QuestionResponse> getQuestionsByUsername(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
		return questionRepository.findByUser(user).stream().map(questionMapper::mapToDto).collect(toList());
	}
	@Transactional(readOnly = true)
	public List<QuestionResponse> getQuestionsByProductId(String productId) {
		return questionRepository.findByProductId(productId).stream().map(questionMapper::mapToDto).collect(toList());
	}
	
	
}