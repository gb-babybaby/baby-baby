package com.app.babybaby.repository.board.review;

import com.app.babybaby.domain.boardDTO.parentsBoardDTO.ParentsBoardDTO;
import com.app.babybaby.entity.board.parentsBoard.ParentsBoard;
import com.app.babybaby.entity.board.review.Review;
import com.app.babybaby.search.admin.AdminReviewSearch;
import com.app.babybaby.search.board.parentsBoard.ParentsBoardSearch;
import com.app.babybaby.type.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface ReviewQueryDsl {

    public Page<Review> findReviewById_QueryDSL(Pageable pageable,Long memberId);

    public Slice<Review> findAllByMemberId_QueryDSL(Pageable pageable, Long memberId);
    /* ---------------------------회원 상세 ----------------------- */
//  [회원상세] 해당 이벤트의 아이디로 모든 리뷰 가져오기
    public List<Review> findAllReivewByEventId(Long eventId);
    
//    [회원 상세] 해당 사람이 올린 모든 체험학습의 모든 리뷰 페이징 처리
    public List<Review> findAllReviewByMemberId_QueryDSL(Long memberId, Pageable pageable);
    
//    [회원 상세] 해당 사람이 올린 모든 체험학습의 모든 리뷰 갯수 구하기
    public Long countAllReviewsByMemberId_QueryDSL(Long memberId);

    public Long findAllReviewCountByMemberId_QueryDSL(Long memberId);

    public Long findAllParentsBoardCountByMemberId_QueryDSL(Long memberId);


    /* ---------------------------회원 상세 ----------------------- */
//  [후기게시판] 리스트 페이징처리
    public Page<Review> findAllReviewWithSearch_QueryDsl(Pageable pageable, ParentsBoardSearch parentsBoardSearch);

    /* 후기게시판  */
//    상세보기 카테고리 최신글 2개 가져오기
    public List<Review> find2RecentDesc(CategoryType category);

    //    상세보기
    public Optional<Review> findDetailById_QueryDsl(Long id);

//    ----------------------------- 관리자 -------------------------------------------

    //[관리자] 리뷰 게시판 목록 조회
    public Page<Review> findAllReviewBoardWithSearch_queryDSL(Pageable pageable, AdminReviewSearch adminReviewSearch, CategoryType eventCategory);

    //[관리자] 리뷰 게시판  상세
    public Optional<Review> findReviewBoardById_queryDSL(Long reviewBoardId);

    //[관리자] 리뷰 게시판 삭제
    public void deleteReviewBoardByIds_queryDSL(Long reviewBoardId);
}
