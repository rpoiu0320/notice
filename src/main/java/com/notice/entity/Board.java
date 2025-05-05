package com.notice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Board {
	
	public enum SearchType {
		TITLE,
		CONTENT,
		WRITER
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@ToString.Exclude
	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	@ManyToOne
	@ToString.Exclude
	@JoinColumn(name = "user_id")
	private User user;
	
	private int views;

	@CreationTimestamp
	private LocalDateTime createdTime;

	@ToString.Exclude
	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();
}