package com.notice.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String content;

	private LocalDateTime createdDate;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User writer;

	@ManyToOne
	@JoinColumn(name = "board_id")
	private Board board;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Comment parent;

					//	parentがfk、		sqlのcascadeと一緒
	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
	private List<Comment> replies;
}
