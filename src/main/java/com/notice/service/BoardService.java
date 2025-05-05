package com.notice.service;

import com.notice.entity.Board;
import com.notice.entity.Board.SearchType;
import com.notice.entity.User;
import com.notice.repository.BoardRepository;
import com.notice.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardRepository boardRepository;
	private final UserRepository userRepository;

	// 게시글 저장 (새로운 메서드, 사용자 이름으로 저장)
	public Board saveBoard(Board board, String username) {
		User user = userRepository.findByUsername(username);
		if (user != null) {
			board.setUser(user);
			return boardRepository.save(board);
		}
		return null;
	}

	// 게시글 삭제 (기존 컨트롤러에서 사용)
	public void deleteBoard(Long id, String username) {
		Optional<Board> optionalBoard = boardRepository.findById(id);
		if (optionalBoard.isPresent()) {
			Board board = optionalBoard.get();
			if (board.getUser().getUsername().equals(username)) {
				boardRepository.deleteById(id);
			}
		}
	}

	// 새로운 delete 메서드도 유지 (CommentController에서 사용할 수 있음)
	public void delete(Long id, String username) {
		deleteBoard(id, username);
	}

	// 게시글 조회수 증가
	public void increaseViews(Board board) {
		board.setViews(board.getViews() + 1);
		boardRepository.save(board);
	}

	// 게시글 ID로 조회하는 메서드 (기존 컨트롤러에서 사용)
	public Board getBoardById(Long id) {
		return boardRepository.findById(id).orElse(null);
	}

	// 페이징 처리 (기존 컨트롤러에서 사용)
	public Page<Board> getBoardPage(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return boardRepository.findAllByOrderByIdDesc(pageable);
	}

	// 현재 사용자 가져오기
	public User getCurrentUser(HttpSession session) {
		String username = (String) session.getAttribute("loggedInUser");
		if (username != null) {
			return userRepository.findByUsername(username);
		}
		return null;
	}

	public Page<Board> searchBoards(String keyword, SearchType searchType, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		switch (searchType) {
		case TITLE:
			return boardRepository.findByTitleContainingIgnoreCase(keyword, pageable);
		case CONTENT:
			return boardRepository.findByContentContainingIgnoreCase(keyword, pageable);
		case WRITER:
			return boardRepository.findByUser_UsernameContainingIgnoreCase(keyword, pageable);
		default:
			return boardRepository.findAll(pageable);
		}
	}

}