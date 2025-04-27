package com.notice.service;

import com.notice.entity.Comment;
import com.notice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;

	public List<Comment> findByBoardId(Long boardId) {
		return commentRepository.findByBoardId(boardId);
	}

	public Comment save(Comment comment) {
		return commentRepository.save(comment);
	}

	public void delete(Long id) {
		commentRepository.deleteById(id);
	}
}
