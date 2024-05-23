package com.eincrm.service;


import com.eincrm.model.Board;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BoardService {


    List<Board> findPostsByCategory(String category);
    List<Board> findAllPosts(); // 모든 게시글 조회
    Board selectPostById(String id); // 글 읽기 기능 추가
    List<Board> selectComment(String id);
    void insertPost(Board board); // 글쓰기 기능 추가
    Board saveComment(Board board);
}
