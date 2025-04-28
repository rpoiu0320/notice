package com.notice.controller;

import com.notice.entity.Comment;
import com.notice.service.CommentService;
import com.notice.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

	private final CommentService commentService;
	private final BoardService boardService;

	@PostMapping("/write/{boardId}")
	public String write(@PathVariable Long boardId, @RequestParam String content, Principal principal) {
		Comment comment = Comment.builder().build();
		comment.setBoard(boardService.findById(boardId).orElse(null));
//		comment.setAuthor(principal.getName());
		comment.setContent(content);
		commentService.save(comment);
		
		return "redirect:/board/detail/" + boardId;
	}

	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Long id, @RequestParam Long boardId) {
		commentService.delete(id);
		
		return "redirect:/board/detail/" + boardId;
	}
}
