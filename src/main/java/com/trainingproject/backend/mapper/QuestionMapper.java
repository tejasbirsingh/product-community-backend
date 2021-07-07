package com.trainingproject.backend.mapper;

import static com.trainingproject.backend.model.VoteType.DOWNVOTE;
import static com.trainingproject.backend.model.VoteType.UPVOTE;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.trainingproject.backend.dto.QuestionRequest;
import com.trainingproject.backend.dto.QuestionResponse;
import com.trainingproject.backend.model.Category;
import com.trainingproject.backend.model.Question;
import com.trainingproject.backend.model.User;
import com.trainingproject.backend.model.Vote;
import com.trainingproject.backend.model.VoteType;
import com.trainingproject.backend.repository.AnswerRepository;
import com.trainingproject.backend.repository.VoteRepository;
import com.trainingproject.backend.service.AuthService;

@Mapper(componentModel = "spring")
public abstract class QuestionMapper {

	@Autowired
	private AnswerRepository answerRepository;
	@Autowired
	private VoteRepository voteRepository;
	@Autowired
	private AuthService authService;

	@Mappings({ @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())"),
			@Mapping(target = "description", source = "questionRequest.description"),
			@Mapping(target = "category", source = "category"), @Mapping(target = "voteCount", constant = "0"),
			@Mapping(target = "user", source = "user"), 
			@Mapping(target="productId",source="questionRequest.productId"),
			@Mapping(target="closed", constant="false")})

	public abstract Question map(QuestionRequest questionRequest, Category category, User user);

	@Mappings({ @Mapping(target = "id", source = "questionId"),
			@Mapping(target = "categoryName", source = "category.name"),
			@Mapping(target = "userName", source = "user.username"),
			@Mapping(target = "answerCount", expression = "java(answerCount(question))"),
			@Mapping(target = "duration", expression = "java(getDuration(question))"),
			@Mapping(target = "upVote", expression = "java(isQuestionUpVoted(question))"),
			@Mapping(target = "downVote", expression = "java(isQuestionDownVoted(question))") ,
			@Mapping(target = "closed", expression = "java(isAnswered(question))") ,
			@Mapping(target ="answerId", source="question.answerId"),
			@Mapping(target="productId",source="question.productId"),

			})

	public abstract QuestionResponse mapToDto(Question question);

	Integer answerCount(Question question) {
		return answerRepository.findByQuestion(question).size();
	}
	
	boolean isAnswered(Question question) {
	    return question.isClosed();
	}
	String getDuration(Question question) {
		return TimeAgo.using(question.getCreatedDate().toEpochMilli());
	}

	boolean isQuestionUpVoted(Question question) {
		return checkVoteType(question, UPVOTE);
	}

	boolean isQuestionDownVoted(Question question) {
		return checkVoteType(question, DOWNVOTE);
	}

	private boolean checkVoteType(Question question, VoteType voteType) {
		if (authService.isLoggedIn()) {
			Optional<Vote> voteForquestionByUser = voteRepository.findTopByQuestionAndUserOrderByVoteIdDesc(question,
					authService.getCurrentUser());
			return voteForquestionByUser.filter(vote -> vote.getVoteType().equals(voteType)).isPresent();
		}
		return false;
	}

}