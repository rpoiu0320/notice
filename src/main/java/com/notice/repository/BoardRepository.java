package com.notice.repository;

import com.notice.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository		//	DBに接近
public interface BoardRepository extends JpaRepository<Board, Long> {
}
