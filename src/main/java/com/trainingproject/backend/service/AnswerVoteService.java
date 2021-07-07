package com.trainingproject.backend.service;

import static com.trainingproject.backend.model.VoteType.UPVOTE;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trainingproject.backend.dto.AnswerVoteDto;
import com.trainingproject.backend.exceptions.ProductWebsiteException;
import com.trainingproject.backend.exceptions.QuestionNotFoundException;
import com.trainingproject.backend.model.Answer;
import com.trainingproject.backend.model.AnswerVote;
import com.trainingproject.backend.repository.AnswerRepository;
import com.trainingproject.backend.repository.AnswerVoteRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AnswerVoteService {

	private final AnswerVoteRepository answerVoteRepository;
	private final AnswerRepository answerRepository;
	private final AuthService authService;

	@Transactional
	public void vote(AnswerVoteDto answerVoteDto) {
//		System.err.println(commentvoteDto.getCommentId());
		Answer answer = answerRepository.findById(answerVoteDto.getAnswerId()).orElseThrow(
				() -> new QuestionNotFoundException("Answer Not Found with ID - " + answerVoteDto.getAnswerId()));

		Optional<AnswerVote> voteByAnswerAndUser = answerVoteRepository
				.findTopByAnswerAndUserOrderByVoteIdDesc(answer, authService.getCurrentUser());

		if (voteByAnswerAndUser.isPresent()
				&& voteByAnswerAndUser.get().getVoteType().equals(answerVoteDto.getVoteType())) {
			throw new ProductWebsiteException("You have already" + answerVoteDto.getVoteType() + "'d for this answer");
		}

		if (UPVOTE.equals(answerVoteDto.getVoteType())) {
			answer.setVoteCount(answer.getVoteCount() + 1);
		} else {
			answer.setVoteCount(answer.getVoteCount() - 1);
		}

		answerVoteRepository.save(mapToVote(answerVoteDto, answer));
		answerRepository.save(answer);
	}

	private AnswerVote mapToVote(AnswerVoteDto commentvoteDto, Answer answer) {
		return AnswerVote.builder().voteType(commentvoteDto.getVoteType()).answer(answer)
				.user(authService.getCurrentUser()).build();
	}
}
