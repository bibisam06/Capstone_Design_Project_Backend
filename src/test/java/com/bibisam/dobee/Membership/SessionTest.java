package com.bibisam.dobee.Membership;

import com.bibisam.dobee.DTO.User.JoinRequest;
import com.bibisam.dobee.DTO.User.LoginRequest;
import com.bibisam.dobee.Service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

//로그인-로그아웃 테스트
@SpringBootTest
public class SessionTest {

    @Autowired
    UserService userService;

    @AfterEach
    @Rollback  // 트랜잭션 롤백
    public void tearDown() {
        // 모든 사용자 삭제
        userService.deleteallUser();
    }

    @DisplayName("세션 로그인 테스트")
    @Test
    public void login() {
        userService.deleteallUser();
        //given
        JoinRequest jr = new JoinRequest();
        jr.setRole("HEAD1");
        jr.setUserPw("ss");
        jr.setUserName("hanbijeong");
        jr.setUserId("111");
        //then
        userService.join(jr);
        LoginRequest lr = new LoginRequest();
        lr.setUserId(jr.getUserId());
        lr.setUserPw(jr.getUserPw());
        //when
        userService.login(lr);

    }

    @DisplayName("로그아웃테스트")
    @Test
    public void logout() {
        System.out.println("dd");
    }
}
