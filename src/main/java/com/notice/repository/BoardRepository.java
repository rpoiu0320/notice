package com.notice.repository;

import com.notice.entity.Board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository // DBに接近
public interface BoardRepository extends JpaRepository<Board, Long> {
	Page<Board> findAllByOrderByIdDesc(Pageable pageable);

	Page<Board> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

	Page<Board> findByContentContainingIgnoreCase(String keyword, Pageable pageable);
	
	Page<Board> findByUser_UsernameContainingIgnoreCase(String keyword, Pageable pageable);
}