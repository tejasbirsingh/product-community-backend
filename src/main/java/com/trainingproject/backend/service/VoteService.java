package com.trainingproject.backend.service;

import static com.trainingproject.backend.model.VoteType.UPVOTE;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trainingproject.backend.dto.VoteDto;
import com.trainingproject.backend.exceptions.QuestionNotFoundException;
import com.trainingproject.backend.exceptions.ProductWebsiteException;
import com.trainingproject.backend.model.Question;
import com.trainingproject.backend.model.Vote;
import com.trainingproject.backend.repository.QuestionRepository;
import com.trainingproject.backend.repository.VoteRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VoteService {

	private final VoteRepository voteRepository;
	private final QuestionRepository questionRepository;
	private final AuthService authService;

	@Transactional
	public void vote(VoteDto voteDto) {
		Question question = questionRepository.findById(voteDto.getQuestionId())
				.orElseThrow(() -> new QuestionNotFoundException("Post Not Found with ID - " + voteDto.getQuestionId()));
		Optional<Vote> voteByQuestionAndUser = voteRepository.findTopByQuestionAndUserOrderByVoteIdDesc(question,
				authService.getCurrentUser());
		if (voteByQuestionAndUser.isPresent() && voteByQuestionAndUser.get().getVoteType().equals(voteDto.getVoteType())) {
			throw new ProductWebsiteException("You have already" + voteDto.getVoteType() + "'d for this post");
		}
		if (UPVOTE.equals(voteDto.getVoteType())) {
			question.setVoteCount(question.getVoteCount() + 1);
		} else {
			question.setVoteCount(question.getVoteCount() - 1);
		}
		voteRepository.save(mapToVote(voteDto, question));
		questionRepository.save(question);
	}

	private Vote mapToVote(VoteDto voteDto, Question question) {
		return Vote.builder().voteType(voteDto.getVoteType()).question(question).user(authService.getCurrentUser()).build();
	}
}
