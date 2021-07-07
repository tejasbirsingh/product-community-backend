package com.trainingproject.backend.model;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Answer {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	@NotEmpty 
	private String text;
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "questionId", referencedColumnName = "questionId")
	private Question question;
	private Instant createdDate;
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "userId", referencedColumnName = "userId")
	private User user;
	private Integer voteCount = 0;
	private boolean accepted = false;
}