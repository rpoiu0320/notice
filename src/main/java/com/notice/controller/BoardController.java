package com.notice.controller;

import com.notice.entity.Board;
import com.notice.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

	private final BoardService boardService;

	@GetMapping("/list")
	public String list(Model model) {
		List<Board> boards = boardService.findAll();
		model.addAttribute("boards", boards);
		return "board/list";
	}

	@GetMapping("/write")
	public String writeForm(Model model) {
		model.addAttribute("board", Board.builder());
		return "board/write";
	}

	@PostMapping("/write")
	public String writeSubmit(@ModelAttribute Board board, Principal principal) {
//		board.setAuthor(principal.getName());
		boardService.save(board);
		return "redirect:/board/list";
	}

	@GetMapping("/detail/{id}")
	public String detail(@PathVariable Long id, Model model) {
		boardService.findById(id).ifPresent(board -> model.addAttribute("board", board));
		return "board/detail";
	}

	@GetMapping("/edit/{id}")
	public String editForm(@PathVariable Long id, Model model) {
		boardService.findById(id).ifPresent(board -> model.addAttribute("board", board));
		return "board/edit";
	}

	@PostMapping("/edit/{id}")
	public String editSubmit(@PathVariable Long id, @ModelAttribute Board board) {
		board.setId(id);
		boardService.save(board);
		return "redirect:/board/detail/" + id;
	}

	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Long id) {
		boardService.delete(id);
		return "redirect:/board/list";
	}
}
