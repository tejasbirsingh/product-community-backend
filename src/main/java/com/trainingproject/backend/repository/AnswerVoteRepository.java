package com.trainingproject.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trainingproject.backend.model.Answer;
import com.trainingproject.backend.model.AnswerVote;
import com.trainingproject.backend.model.User;

@Repository
public interface AnswerVoteRepository extends JpaRepository<AnswerVote, Long> {
	Optional<AnswerVote> findTopByAnswerAndUserOrderByVoteIdDesc(Answer comment, User currentUser);

}
