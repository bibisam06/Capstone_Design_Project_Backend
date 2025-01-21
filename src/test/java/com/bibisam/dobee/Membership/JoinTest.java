package com.bibisam.dobee.Membership;

import com.bibisam.dobee.DTO.User.JoinRequest;
import com.bibisam.dobee.Entity.Users;
import com.bibisam.dobee.Service.UserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

//회원 기능 관련 테스트
@ExtendWith(SpringExtension.class)
@SpringBootTest
class JoinTest {

    JoinTest(UserService userService, PasswordEncoder encoder, EntityManager entityManager) {
        this.userService = userService;
    }

    @AfterEach
    @Rollback  // 트랜잭션 롤백
    public void tearDown() {
        // 모든 사용자 삭제
        userService.deleteallUser();
    }



    private final UserService userService;

    @Test
    public void join_test() {
        //회원 가입 기능 테스트
        //given
        JoinRequest jr = new JoinRequest();
        //then
        jr.setRole("HEAD1");
        jr.setUserPw("ss");
        jr.setUserName("hanbijeong");
        jr.setUserId("111");
        //when
        userService.join(jr);
    }

    @Test
    public void check_userid_duplicated(){
        //given
        JoinRequest jr1 = new JoinRequest();
        JoinRequest jr2 = new JoinRequest();

        //then
        jr1.setRole("HEAD1");
        jr1.setUserPw("ss");
        jr1.setUserName("hanbijeong");
        jr1.setUserId("111");

        jr2.setRole("HEAD1");
        jr2.setUserPw("ss");
        jr2.setUserName("hanbijeong");
        jr2.setUserId("112");
        //when
        userService.join(jr1);
        userService.join(jr2);

    }

    //TODO:시퀀스 수정하기
    @Test
        public void user_id_sequence(){
            //회원 가입 기능 테스트
            //given
            JoinRequest jr = new JoinRequest();
            //then
            jr.setRole("HEAD1");
            jr.setUserPw("ss");
            jr.setUserName("hanbijeong");
            jr.setUserId("111");
            //when
            Users testUser = userService.join(jr);

            assertThat(testUser.getId()).isEqualTo(1);
    }

    @Test
    public void delete_all(){
        userService.deleteallUser();
        JoinRequest jr = new JoinRequest();
        //then
        jr.setRole("HEAD1");
        jr.setUserPw("ss");
        jr.setUserName("hanbijeong");
        jr.setUserId("111");
        //when
        Users testUser = userService.join(jr);

        assertThat(testUser.getId()).isEqualTo(1);
    }

    @DisplayName("회원탈퇴 테스트")
    @Test
    public void delete_users(){
        userService.deletebyUserId("jade");
    }

    @Test
    @DisplayName("아이디 찾기")
    public void find_id() {

    userService.findUserByEmail("user@example.com");
    }

    @Test
    @DisplayName("비밀번호 찾기")
    public void find_pw(){

    }



    }
