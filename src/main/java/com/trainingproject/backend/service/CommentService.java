package com.trainingproject.backend.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.trainingproject.backend.dto.CommentRequest;
import com.trainingproject.backend.dto.CommentResponse;
import com.trainingproject.backend.exceptions.QuestionNotFoundException;
import com.trainingproject.backend.mapper.CommentMapper;
import com.trainingproject.backend.model.Comment;
import com.trainingproject.backend.model.NotificationEmail;
import com.trainingproject.backend.model.Question;
import com.trainingproject.backend.model.User;
import com.trainingproject.backend.repository.CommentRepository;
import com.trainingproject.backend.repository.QuestionRepository;
import com.trainingproject.backend.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentService {
	private static final String POST_URL = "";
	private final QuestionRepository questionRepository;
	private final UserRepository userRepository;
	private final AuthService authService;
	private final CommentMapper commentMapper;
	private final CommentRepository commentRepository;
	private final MailContentBuilder mailContentBuilder;
	private final MailService mailService;

	public void save(CommentRequest commentsDto) {
		Question question = questionRepository.findById(commentsDto.getQuestionId())
				.orElseThrow(() -> new QuestionNotFoundException(commentsDto.getQuestionId().toString()));
		Comment comment = commentMapper.map(commentsDto, question, authService.getCurrentUser());
		comment.setVoteCount(0);
		commentRepository.save(comment);

		String message = mailContentBuilder
				.build(authService.getCurrentUser() + " answered the question posted by you" + POST_URL);
		sendCommentNotification(message, question.getUser());
	}
	
	public void accept(Long id) {
	    Comment acceptedAnswer = commentRepository.findById(id)
		    .orElseThrow(() -> new QuestionNotFoundException(id.toString()));;
	    boolean isAccepted = acceptedAnswer.isAccepted();
	    acceptedAnswer.setAccepted(isAccepted ? false : true);
	    commentRepository.save(acceptedAnswer);
	}

	private void sendCommentNotification(String message, User user) {
		mailService.sendMail(
				new NotificationEmail(user.getUsername() + " Answered on your question", user.getEmail(), message));
	}

	public List<CommentResponse> getAllCommentsForQuestion(Long postId) {
		Question question = questionRepository.findById(postId).orElseThrow(() -> new QuestionNotFoundException(postId.toString()));
		return commentRepository.findByQuestion(question).stream().map(commentMapper::mapToDto).collect(toList());
	}

	public List<CommentResponse> getAllCommentsForUser(String userName) {
		User user = userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException(userName));
		return commentRepository.findAllByUser(user).stream().map(commentMapper::mapToDto).collect(toList());
	}

	public CommentResponse getComment(Long id) {
		Comment comment = commentRepository.findById(id).orElseThrow(() -> new QuestionNotFoundException(id.toString()));
		return commentMapper.mapToDto(comment);

	}

}