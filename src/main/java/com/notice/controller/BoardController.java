package com.notice.controller;

import com.notice.entity.Comment;
import com.notice.entity.Board;
import com.notice.entity.User;
import com.notice.entity.Board.SearchType;
import com.notice.service.CommentService;
import com.notice.service.BoardService;
import com.notice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping
public class BoardController {

	@Autowired
	private BoardService boardService;

	@Autowired
	private UserService userService;

	@Autowired
	private CommentService commentService;

	@GetMapping("/")
	public String home(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size,
			@RequestParam(name = "keyword", defaultValue = "") String keyword,
			@RequestParam(name = "searchType", required = false) SearchType searchType, Model model,
			HttpSession session) {
		Page<Board> boardPage;

		if (!keyword.isBlank() && keyword != null) {
			searchType = searchType == null ? SearchType.TITLE : searchType; 
			boardPage = boardService.searchBoards(keyword, searchType, page, size);
			model.addAttribute("keyword", keyword);
			model.addAttribute("searchType", searchType);
		} else 
			boardPage = boardService.getBoardPage(page, size);
		
		model.addAttribute("boards", boardPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", boardPage.getTotalPages());
		model.addAttribute("totalElements", boardPage.getTotalElements());
		model.addAttribute("pageSize", size);

		// 현재 로그인한 사용자 정보를 모델에 추가
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
			session.setAttribute("loggedInUser", auth.getName());
			model.addAttribute("name", userService.getCurrentUser(session).getName());
		} else
			session.setAttribute("anonymous", auth.getName());

		return "index";
	}

	@GetMapping("/board/write")
	public String writeForm(Model model) {
		model.addAttribute("board", new Board());
		return "write";
	}

	@PostMapping("/board/write")
	public String write(@ModelAttribute Board board, HttpSession session) {
		String username = (String) session.getAttribute("loggedInUser");
		User user = userService.getCurrentUser(session);
		if (user == null) {
			return "redirect:/login";
		}

		board.setUser(user);
		boardService.saveBoard(board, username);
		return "redirect:/";
	}

	@GetMapping("/board/{id}")
	public String detail(@PathVariable("id") Long id, Model model, HttpSession session) {
		Board board = boardService.getBoardById(id);
		if (board == null) {
			return "redirect:/";
		}

		boardService.increaseViews(board);
		List<Comment> comments = commentService.getCommentsByBoard(board);

		model.addAttribute("board", board);
		model.addAttribute("comments", comments);
		model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));

		return "detail";
	}

	@GetMapping("/board/edit/{id}")
	public String editForm(@PathVariable("id") Long id, HttpSession session, Model model) {
		String username = (String) session.getAttribute("loggedInUser");
		if (username == null) {
			return "redirect:/login";
		}

		Board board = boardService.getBoardById(id);
		if (board == null || !board.getUser().getUsername().equals(username)) {
			return "redirect:/";
		}

		model.addAttribute("board", board);
		return "edit";
	}

	@PostMapping("/board/edit/{id}")
	public String edit(@PathVariable("id") Long id, @ModelAttribute Board updatedBoard, HttpSession session) {
		String username = (String) session.getAttribute("loggedInUser");
		if (username == null) {
			return "redirect:/login";
		}

		Board board = boardService.getBoardById(id);
		if (board == null || !board.getUser().getUsername().equals(username)) {
			return "redirect:/";
		}

		board.setTitle(updatedBoard.getTitle());
		board.setContent(updatedBoard.getContent());
		boardService.saveBoard(board, username);

		return "redirect:/board/" + id;
	}

	@GetMapping("/board/delete/{id}")
	public String delete(@PathVariable("id") Long id, HttpSession session) {
		String username = (String) session.getAttribute("loggedInUser");
		if (username == null) {
			return "redirect:/login";
		}

		boardService.deleteBoard(id, username);
		return "redirect:/";
	}

	@PostMapping("/comment/write")
	public String writeComment(@RequestParam("content") String content, @RequestParam("boardId") Long boardId,
			HttpSession session) {
		String username = (String) session.getAttribute("loggedInUser");
		if (username == null) {
			return "redirect:/login";
		}
		commentService.saveComment(content, boardId, username);
		return "redirect:/board/" + boardId;
	}

	@GetMapping("/comment/delete/{commentId}")
	public String deleteComment(@PathVariable("commentId") Long commentId, @RequestParam("boardId") Long boardId,
			HttpSession session) {
		String username = (String) session.getAttribute("loggedInUser");
		if (username == null) {
			return "redirect:/login";
		}

		commentService.deleteComment(commentId, username);
		return "redirect:/board/" + boardId;
	}
}