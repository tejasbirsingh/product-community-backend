package com.trainingproject.backend.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.trainingproject.backend.dto.AnswerRequest;
import com.trainingproject.backend.dto.AnswerResponse;
import com.trainingproject.backend.exceptions.QuestionNotFoundException;
import com.trainingproject.backend.mapper.AnswerMapper;
import com.trainingproject.backend.model.Answer;
import com.trainingproject.backend.model.NotificationEmail;
import com.trainingproject.backend.model.Question;
import com.trainingproject.backend.model.User;
import com.trainingproject.backend.repository.AnswerRepository;
import com.trainingproject.backend.repository.QuestionRepository;
import com.trainingproject.backend.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AnswerService {
	private static final String POST_URL = "http://localhost:4200/view-question/";
	private final QuestionRepository questionRepository;
	private final UserRepository userRepository;
	private final AuthService authService;
	private final AnswerMapper answerMapper;
	private final AnswerRepository answerRepository;
	private final MailContentBuilder mailContentBuilder;
	private final MailService mailService;

	public void save(AnswerRequest answersDto) {
		Question question = questionRepository.findById(answersDto.getQuestionId())
				.orElseThrow(() -> new QuestionNotFoundException(answersDto.getQuestionId().toString()));
		Answer answer = answerMapper.map(answersDto, question, authService.getCurrentUser());
		answer.setVoteCount(0);
		answerRepository.save(answer);

		String message = mailContentBuilder
				.build(authService.getCurrentUser().getUsername() + " answered the question posted by you",POST_URL + question.getQuestionId());
		sendAnswerNotification(message, question.getUser());
	}
	
	public void accept(Long id) {
	    Answer acceptedAnswer = answerRepository.findById(id)
		    .orElseThrow(() -> new QuestionNotFoundException(id.toString()));;
	    boolean isAccepted = acceptedAnswer.isAccepted();
	    acceptedAnswer.setAccepted(isAccepted ? false : true);
	    answerRepository.save(acceptedAnswer);
	}

	private void sendAnswerNotification(String message, User user) {
		mailService.sendMail(
				new NotificationEmail(user.getUsername() + " Answered on question", user.getEmail(), message));
	}

	public List<AnswerResponse> getAllAnswersForQuestion(Long postId) {
		Question question = questionRepository.findById(postId).orElseThrow(() -> new QuestionNotFoundException(postId.toString()));
		return answerRepository.findByQuestion(question).stream().map(answerMapper::mapToDto).collect(toList());
	}

	public List<AnswerResponse> getAllAnswersForUser(String userName) {
		User user = userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException(userName));
		return answerRepository.findAllByUser(user).stream().map(answerMapper::mapToDto).collect(toList());
	}

	public AnswerResponse getAnswer(Long id) {
		Answer answer = answerRepository.findById(id).orElseThrow(() -> new QuestionNotFoundException(id.toString()));
		return answerMapper.mapToDto(answer);

	}

}