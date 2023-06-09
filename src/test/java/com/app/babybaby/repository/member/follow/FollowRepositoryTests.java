package com.app.babybaby.repository.member.follow;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
@Transactional
@Rollback(false)
public class FollowRepositoryTests {
    @Autowired
    FollowRepository followRepository;

    @Test
    public void followTest(){
        log.info(String.valueOf(followRepository.findFollowerMemberCountByMemberId_QueryDSL(1L)));
        log.info(String.valueOf(followRepository.findFollowingMemberCountByMemberId_QueryDSL(1L)));
    }
}
