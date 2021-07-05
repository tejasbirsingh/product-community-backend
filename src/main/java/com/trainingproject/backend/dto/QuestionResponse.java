package com.trainingproject.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponse {
	private Long id;
	private String questionName;
	private String url;
	private String description;
	private String userName;
	private String categoryName;
	private Integer voteCount;
	private Integer commentCount;
	private String duration;
	private boolean upVote;
	private boolean downVote;
	private boolean isClosed;
	private Long answerId;
	private String productId;
}
