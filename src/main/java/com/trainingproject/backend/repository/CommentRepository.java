package com.trainingproject.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trainingproject.backend.model.Comment;
import com.trainingproject.backend.model.Question;
import com.trainingproject.backend.model.User;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    List<Comment> findByQuestion(Question question);

    List<Comment> findAllByUser(User user);
}
