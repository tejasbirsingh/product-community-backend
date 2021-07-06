package com.trainingproject.backend.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long userId;
	@Column(unique=true)
	@NotBlank(message = "Username is required")
	private String username;
	@NotBlank(message = "Password is required")
	private String password;
	@Column(unique = true)
	@Email
	@NotEmpty(message = "Email is required")
	private String email;
	private Instant created;
	private boolean enabled;
}