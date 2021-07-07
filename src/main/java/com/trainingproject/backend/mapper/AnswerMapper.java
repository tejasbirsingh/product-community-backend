package com.trainingproject.backend.mapper;

import static com.trainingproject.backend.model.VoteType.DOWNVOTE;
import static com.trainingproject.backend.model.VoteType.UPVOTE;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.trainingproject.backend.dto.AnswerRequest;
import com.trainingproject.backend.dto.AnswerResponse;
import com.trainingproject.backend.model.Answer;
import com.trainingproject.backend.model.AnswerVote;
import com.trainingproject.backend.model.Question;
import com.trainingproject.backend.model.User;
import com.trainingproject.backend.model.VoteType;
import com.trainingproject.backend.repository.AnswerVoteRepository;
import com.trainingproject.backend.service.AuthService;

@Mapper(componentModel = "spring")
public abstract class AnswerMapper {


	@Autowired
	private AnswerVoteRepository voteRepository;
	@Autowired
	private AuthService authService;

	@Mappings({ @Mapping(target = "id", ignore = true), @Mapping(target = "text", source = "answersDto.text"),
			@Mapping(target = "createdDate", expression = "java(java.time.Instant.now())"),
			@Mapping(target = "question", source = "question"), @Mapping(target = "user", source = "user"),
			@Mapping(target = "voteCount", constant = "0") ,
			@Mapping(target = "accepted", constant="false")})

	public abstract Answer map(AnswerRequest answersDto, Question question, User user);

	@Mappings({ @Mapping(target = "questionId", expression = "java(answer.getQuestion().getQuestionId())"),
			@Mapping(target = "userName", expression = "java(answer.getUser().getUsername())"),
			@Mapping(target = "upVote", expression = "java(isAnswerUpVoted(answer))"),
			@Mapping(target = "downVote", expression = "java(isAnswerDownVoted(answer))"),
			@Mapping(target ="accepted", expression="java(isAnswered(answer))"),
			@Mapping(target = "createdDate", expression = "java(getDuration(answer))"),
})
	public abstract AnswerResponse mapToDto(Answer answer);

	boolean isAnswerUpVoted(Answer answer) {
		return checkVoteType(answer, UPVOTE);
	}
	String getDuration(Answer answer) {
		return TimeAgo.using(answer.getCreatedDate().toEpochMilli());
	}

	boolean isAnswered(Answer answer) {
	    return answer.isAccepted();
	}
	boolean isAnswerDownVoted(Answer answer) {
		return checkVoteType(answer, DOWNVOTE);
	}

	private boolean checkVoteType(Answer answer, VoteType voteType) {
		if (authService.isLoggedIn()) {
			Optional<AnswerVote> voteForQuestionByUser = voteRepository.findTopByAnswerAndUserOrderByVoteIdDesc(answer,
					authService.getCurrentUser());
			return voteForQuestionByUser.filter(vote -> vote.getVoteType().equals(voteType)).isPresent();
		}
		return false;
	}
}