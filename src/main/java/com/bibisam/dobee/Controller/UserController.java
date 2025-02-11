//UserController
package com.bibisam.dobee.Controller;

import com.bibisam.dobee.DTO.User.MyPageResponse;
import com.bibisam.dobee.Entity.Users;
import com.bibisam.dobee.Repository.RedisRepository;
import com.bibisam.dobee.Service.EmailService;
import com.bibisam.dobee.Service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    private final EmailService emailService;

    private final RedisRepository redisRepository;

    private final PasswordEncoder encoder;



    //TODO : 403 뜬다고 함 -> 브라우저에서 ..
    @GetMapping("/logout")
    public ResponseEntity<Map<String,Object>> sessionLogout(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try{
            if(session != null) {
                System.out.println("세션 존재함");
                session.invalidate();
            }
            response.put("success", "sessionLogout successed");
            return ResponseEntity.ok(response);
        }catch(Exception e){
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    @Transactional
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable String  id){
        userService.deletebyUserId(id);
    }


    //비밀번호 수정
    @PatchMapping("/change_pw")
    public ResponseEntity<Map<String, Object>> changePw(@RequestBody String userId, String userPw) {
        Map<String, Object> response = new HashMap<>();
        Users findUser = userService.findByUserId(userId);
        if(findUser == null){
            response.put("message", "해당 유저가 존재하지 않습니다.");
            response.put("userId", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        else { //해당 유저 조회성공 한 경우,
            findUser.setUserPw(userPw);
            response.put("message","해당 유저를 조회 성공하였습니다.");
            response.put("status", "200");
            response.put("new password", findUser.getUserPw());
            return ResponseEntity.ok(response);
        }


    }

    //TODO : 정보바꾸기
    @PatchMapping("/change-detail")
    public void changeId(){

    }

    //TODO : 마이페이지정보..갖다주기
    @GetMapping("/mypage")
    public ResponseEntity<MyPageResponse> myPage(@RequestParam String userId){
        Users foundUser = userService.findByUserId(userId); //user찾아가지고..
        MyPageResponse response = new MyPageResponse();
        response.setUserName(foundUser.getUserName());
        response.setRole(foundUser.getRole());
        response.setUserId(foundUser.getUserId());
        response.setEmail(foundUser.getEmail());
        response.setUserStatus(foundUser.getUserStatus());
        response.setPhoneNumber(foundUser.getPhoneNumber());
        response.setMyVoteList(foundUser.getUsersList());
        response.setAssociation(foundUser.getAssociation());

        return ResponseEntity.ok(response);
    }



}
