package com.trainingproject.backend.mapper;

import static com.trainingproject.backend.model.VoteType.DOWNVOTE;
import static com.trainingproject.backend.model.VoteType.UPVOTE;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.trainingproject.backend.dto.CommentRequest;
import com.trainingproject.backend.dto.CommentResponse;
import com.trainingproject.backend.model.Comment;
import com.trainingproject.backend.model.CommentVote;
import com.trainingproject.backend.model.Question;
import com.trainingproject.backend.model.User;
import com.trainingproject.backend.model.VoteType;
import com.trainingproject.backend.repository.CommentVoteRepository;
import com.trainingproject.backend.service.AuthService;

@Mapper(componentModel = "spring")
public abstract class CommentMapper {


	@Autowired
	private CommentVoteRepository voteRepository;
	@Autowired
	private AuthService authService;

	@Mappings({ @Mapping(target = "id", ignore = true), @Mapping(target = "text", source = "commentsDto.text"),
			@Mapping(target = "createdDate", expression = "java(java.time.Instant.now())"),
			@Mapping(target = "question", source = "question"), @Mapping(target = "user", source = "user"),
			@Mapping(target = "voteCount", constant = "0") ,
			@Mapping(target = "accepted", constant="false")})

	public abstract Comment map(CommentRequest commentsDto, Question question, User user);

	@Mappings({ @Mapping(target = "questionId", expression = "java(comment.getQuestion().getQuestionId())"),
			@Mapping(target = "userName", expression = "java(comment.getUser().getUsername())"),
			@Mapping(target = "upVote", expression = "java(isCommentUpVoted(comment))"),
			@Mapping(target = "downVote", expression = "java(isCommentDownVoted(comment))"),
			@Mapping(target ="accepted", expression="java(isAnswered(comment))"),
			@Mapping(target = "createdDate", expression = "java(getDuration(comment))"),
})
	public abstract CommentResponse mapToDto(Comment comment);

	boolean isCommentUpVoted(Comment comment) {
		return checkVoteType(comment, UPVOTE);
	}
	String getDuration(Comment comment) {
		return TimeAgo.using(comment.getCreatedDate().toEpochMilli());
	}

	boolean isAnswered(Comment comment) {
	    return comment.isAccepted();
	}
	boolean isCommentDownVoted(Comment comment) {
		return checkVoteType(comment, DOWNVOTE);
	}

	private boolean checkVoteType(Comment comment, VoteType voteType) {
		if (authService.isLoggedIn()) {
			Optional<CommentVote> voteForQuestionByUser = voteRepository.findTopByCommentAndUserOrderByVoteIdDesc(comment,
					authService.getCurrentUser());
			return voteForQuestionByUser.filter(vote -> vote.getVoteType().equals(voteType)).isPresent();
		}
		return false;
	}
}