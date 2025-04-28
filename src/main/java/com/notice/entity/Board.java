package com.notice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class Board {

	@Id				//	dbのpk、autoincrementと一緒		TODO:もっと勉強必要
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	@Column(columnDefinition = "TEXT")
	private String content;

	private LocalDateTime createdDate;

	/*
	 * 1:1 @OneToOne 
	 * 1:N @OneToMany 
	 * N:1 @ManyToOne 
	 * N:M @ManyToMany
	 */
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User writer;
}
