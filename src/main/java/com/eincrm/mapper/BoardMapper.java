package com.eincrm.mapper;

import com.eincrm.model.Board;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<Board> findPostsByCategory(@Param("category") String category);
    List<Board> findAllPosts();
    List<Board> noticeBoardList();

    Board selectPostById(String id,String custCode);
    List<Board> selectComment(String custCode,String id);

    void insertComment(Board board);


    void insertPost(Board board);


    int  getNextUno(@Param("catGroup") String catGroup);


    int getReplyCount(@Param("catGroup") String catGroup, @Param("gno") String gno, @Param("replyDepth") String replyDepth);

}
