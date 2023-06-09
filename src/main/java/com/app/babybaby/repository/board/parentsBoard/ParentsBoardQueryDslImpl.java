package com.app.babybaby.repository.board.parentsBoard;

import com.app.babybaby.entity.board.event.Event;
import com.app.babybaby.entity.board.event.QEvent;
import com.app.babybaby.entity.board.nowKids.QNowKids;
import com.app.babybaby.entity.board.parentsBoard.ParentsBoard;
import com.app.babybaby.entity.board.parentsBoard.QParentsBoard;
import com.app.babybaby.entity.member.Member;
import com.app.babybaby.entity.purchase.purchase.QPurchase;
import com.app.babybaby.search.admin.AdminParentsBoardSearch;
import com.app.babybaby.search.board.parentsBoard.ParentsBoardSearch;
import com.app.babybaby.type.CategoryType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.app.babybaby.entity.board.event.QEvent.event;
import static com.app.babybaby.entity.board.nowKids.QNowKids.nowKids;
import static com.app.babybaby.entity.board.parentsBoard.QParentsBoard.parentsBoard;
import static com.app.babybaby.entity.purchase.purchase.QPurchase.purchase;

@RequiredArgsConstructor
@Slf4j
public class ParentsBoardQueryDslImpl implements ParentsBoardQueryDsl {

    private final JPAQueryFactory query;

    @Override
    public Page<ParentsBoard> findAllWithSearch_QueryDsl(Pageable pageable, ParentsBoardSearch parentsBoardSearch) {
        BooleanExpression categoryType = parentsBoardSearch.getCategoryType() == null ? null : parentsBoard.event.category.isNull().or(parentsBoard.event.category.eq(parentsBoardSearch.getCategoryType()));

//        BooleanExpression categoryType = parentsBoardSearch.getCategoryType() == null ? null : parentsBoard.event.category.eq(parentsBoardSearch.getCategoryType());
        BooleanExpression searchTitle = parentsBoardSearch.getSearchTitle() == null ? null : parentsBoard.boardTitle.contains(parentsBoardSearch.getSearchTitle());
        BooleanExpression searchContent = parentsBoardSearch.getSearchContent() == null ? null : parentsBoard.boardContent.contains(parentsBoardSearch.getSearchContent());
        BooleanExpression searchAll = parentsBoardSearch.getSearchContent() == null && parentsBoardSearch.getSearchTitle() == null
                ? null
                :(parentsBoard.boardContent.contains(parentsBoardSearch.getSearchContent())
                .or(parentsBoard.boardTitle.contains(parentsBoardSearch.getSearchTitle())));
//        if(parentsBoardSearch.getCategoryType() == null) {
//            parentsBoardSearch.setCategoryType(CategoryType.TALK);
//        }
        log.info(categoryType + "카테고리임!!!");
        log.info(searchTitle + "검색제목임!!!");
        log.info(searchContent + "검색내용임!!!");

//       전체 목록 불러오기(페이징)
        List<ParentsBoard> foundParentsBoard = query.select(parentsBoard)
                .from(parentsBoard)
                .leftJoin(parentsBoard.event)
                .leftJoin(parentsBoard.parentsBoardFiles)
                .fetchJoin()

                .orderBy(parentsBoard.id.desc())
//                .where(createBooleanExpression(parentsBoardSearch)/*, createTextSearchOption(parentsBoardSearch)*/)
                .where(
                        (searchTitle != null && searchContent != null)
                                ? searchAll
                                : searchTitle.or(searchContent),
                        categoryType
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = query.select(parentsBoard.count())
                .from(parentsBoard)
//                .where(createBooleanExpression(parentsBoardSearch)/*, createTextSearchOption(parentsBoardSearch)*/)
                .where(
                        (searchTitle != null && searchContent != null)
                                ? searchAll
                                : searchTitle.or(searchContent),
                        categoryType
                )
                .fetchOne();
        log.info("asdsadasdd" + foundParentsBoard);
        return new PageImpl<>(foundParentsBoard, pageable, count);
    }

    //    내가쓴 게시글
    @Override
    public Page<ParentsBoard> findParentBoardListByMemberId(Pageable pageable, Long memberId) {

        List<ParentsBoard> foundParentsBoard = query.select(parentsBoard)
                .from(parentsBoard)
                .join(parentsBoard.event)
                .fetchJoin()
                .leftJoin(parentsBoard.parentsBoardFiles)
                .fetchJoin()
                .orderBy(parentsBoard.id.desc())
                .where(parentsBoard.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = query.select(parentsBoard.count())
                .from(parentsBoard)
                .where(parentsBoard.member.id.eq(memberId))
                .fetchOne();

        return new PageImpl<>(foundParentsBoard, pageable, count);
    }

    //    상세보기
    @Override
    public Optional<ParentsBoard> findDetailById_QueryDsl(Long id) {

        return Optional.ofNullable(
                query.select(parentsBoard)
                        .from(parentsBoard)
                        .join(parentsBoard.event)
                        .fetchJoin()
                        .leftJoin(parentsBoard.parentsBoardFiles)
                        .fetchJoin()
                        .join(parentsBoard.member)
                        .fetchJoin()
                        .where(parentsBoard.id.eq(id))
                        .fetchOne()
        );
    }

    //    작성하기 참여예정 체험학습 select 해오기
    @Override
    public List<Event> findAllUpcomingEventsByMemberId(Long id) {
        LocalDateTime now = LocalDateTime.now();
        return
                query.select(purchase.event)
                        .from(purchase)
                        .join(purchase.event)
                        .join(purchase.member)
                        .where(purchase.member.id.eq(id))
                        .where(purchase.event.calendar.startDate.after(now))
                        .where(purchase.event.calendar.endDate.after(now))
                        .orderBy(purchase.event.calendar.startDate.asc())
                        .fetch();
    }

    @Override
    public Page<ParentsBoard> findListByMemberIdWithPaging_QueryDSL(Pageable pageable,Long memberId) {
        List<ParentsBoard> parentsBoards = query.select(parentsBoard)
                .from(parentsBoard)
                .join(parentsBoard.member).fetchJoin()
                .join(parentsBoard.parentsBoardFiles).fetchJoin()
                .where(parentsBoard.member.id.eq(memberId))
                .orderBy(parentsBoard.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = query.select(parentsBoard.count()).from(parentsBoard).where(parentsBoard.member.id.eq(memberId)).fetchOne();


        return new PageImpl<>(parentsBoards, pageable, count);
    }



    // 상세보기 카테고리 최신글 2개 가져오기 where절 손 봐야하나?
    @Override
    public List<ParentsBoard> find2RecentDesc(CategoryType category) {
//        QParentsBoard subParents = new QParents
        List<ParentsBoard> parentsBoards = query.select(parentsBoard)
                .from(parentsBoard)
                .where(parentsBoard.event.category.eq(category))
                .orderBy(parentsBoard.id.desc())
                .limit(2)
                .fetch();
        return parentsBoards;
    }

//    QNowKids subNowKids = new QNowKids("subNowKids");
//    List<Member> subQueryResult = query.select(subNowKids.guide)
//            .from(subNowKids)
//            .orderBy(subNowKids.id.desc())
//            .limit(8)
//            .fetch();
//        return query.selectDistinct(nowKids.guide)
//            .from(nowKids)
//                .where(nowKids.guide.in(subQueryResult))
//            .fetch();


    @Override
    public List<ParentsBoard> find5RecentDesc() {
        List<ParentsBoard> parentsBoards =
                query.select(parentsBoard)
                        .from(parentsBoard)
                        .orderBy(parentsBoard.id.desc())
                        .limit(5)
                        .fetch();
        return parentsBoards;
    }

//---------------------------------------- 관리자 ------------------------------------------------------

    //[관리자] 보호자마당 전체 목록 조회
    @Override
    public Page<ParentsBoard> findAllParentsBoardWithSearch_queryDSL(Pageable pageable, AdminParentsBoardSearch adminParentsBoardSearch) {
        BooleanExpression parentsBoardNameEq = adminParentsBoardSearch.getParentsBoardTitle() == null ? null : parentsBoard.boardTitle.like("%" + adminParentsBoardSearch.getParentsBoardTitle() + "%");

        QParentsBoard parentsBoard = QParentsBoard.parentsBoard;
        QEvent event = QEvent.event;

        List<ParentsBoard> foundParentsBoard = query.select(parentsBoard)
                .from(parentsBoard)
                .join(parentsBoard.event)
                .join(parentsBoard.member)
                .fetchJoin()
                .where(parentsBoardNameEq)
                .orderBy(parentsBoard.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = query.select(parentsBoard.count())
                .from(parentsBoard)
                .where(parentsBoardNameEq)
                .fetchOne();

        return new PageImpl<>(foundParentsBoard, pageable, count);
    }

    //[관리자] 부모님마당 상세 조회
    @Override
    public Optional<ParentsBoard> findParentsBoardById_queryDSL(Long parentsBoardId) {
        return Optional.ofNullable(
                query.select(parentsBoard)
                        .from(parentsBoard)
                        .join(parentsBoard.event)
                        .fetchJoin()
                        .leftJoin(parentsBoard.parentsBoardFiles)
                        .fetchJoin()
                        .join(parentsBoard.member)
                        .fetchJoin()
                        .where(parentsBoard.event.id.eq(parentsBoardId))
                        .fetchOne()
        );
    }

    //  [관리자] 보호자마당 삭제
    @Override
    public void deleteAdminParentsBoardByIds_queryDSL(Long parentsBoardId) {
        query.delete(parentsBoard)
                .where(parentsBoard.id.in(parentsBoardId))
                .execute();
    }


}
