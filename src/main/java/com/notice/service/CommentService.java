package com.notice.service;

import com.notice.entity.Comment;
import com.notice.entity.Board;
import com.notice.entity.User;
import com.notice.repository.CommentRepository;
import com.notice.repository.BoardRepository;
import com.notice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    
    // 기존 컨트롤러에서 사용하는 메서드 유지
    public Comment saveComment(String content, Long boardId, String username) {
        Board board = boardRepository.findById(boardId).orElseThrow();
        User user = userRepository.findByUsername(username);
        
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setBoard(board);
        comment.setUser(user);
        
        return commentRepository.save(comment);
    }
    
    // 새로운 CommentController에서 사용하는 메서드 추가
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }
    
    // 기존 컨트롤러에서 사용하는 메서드 수정 (대소문자 오류 수정)
    public List<Comment> getCommentsByBoard(Board board) {
        return commentRepository.findByBoardOrderByCreatedTimeDesc(board);
    }
    
    // 기존 컨트롤러에서 사용하는 메서드 유지
    public void deleteComment(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (comment.getUser().getUsername().equals(username)) {
            commentRepository.delete(comment);
        }
    }
    
    // 새로운 CommentController에서 사용하는 메서드 추가
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }
}