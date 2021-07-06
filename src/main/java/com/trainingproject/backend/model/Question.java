package com.trainingproject.backend.model;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Question {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long questionId;
	@NotBlank(message = "Question Name cannot be empty or Null")
	private String questionName;
	@Nullable
	@Lob
	private String url;
	@Nullable
	@Lob
	private String description;
	private boolean closed = false;
	private Long answerId;
	private String productId;
	private Integer voteCount = 0;
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "userId", referencedColumnName = "userId")
	private User user;
	private Instant createdDate;
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id")
	private Category category;
}