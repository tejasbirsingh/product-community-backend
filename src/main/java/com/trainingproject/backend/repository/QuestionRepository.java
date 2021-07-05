package com.trainingproject.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trainingproject.backend.model.Category;
import com.trainingproject.backend.model.Question;
import com.trainingproject.backend.model.User;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
	List<Question> findAllByCategory(Category category);

	List<Question> findByUser(User user);

	List<Question> findByClosed(boolean closed);

	List<Question> findByProductId(String productId);
}
