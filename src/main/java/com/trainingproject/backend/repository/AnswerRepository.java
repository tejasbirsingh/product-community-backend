package com.trainingproject.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trainingproject.backend.model.Answer;
import com.trainingproject.backend.model.Question;
import com.trainingproject.backend.model.User;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    
    List<Answer> findByQuestion(Question question);

    List<Answer> findAllByUser(User user);
}
