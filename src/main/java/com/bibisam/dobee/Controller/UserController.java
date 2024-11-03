package com.bibisam.dobee.Controller;

import com.bibisam.dobee.DTO.Auth.EmailRequest;
import com.bibisam.dobee.DTO.Auth.ResponseDto;
import com.bibisam.dobee.DTO.User.JoinRequest;
import com.bibisam.dobee.DTO.User.LoginRequest;
import com.bibisam.dobee.Entity.Users;
import com.bibisam.dobee.Exceptions.User.DuplicateUserIdException;
import com.bibisam.dobee.Service.EmailService;
import com.bibisam.dobee.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.webresources.EmptyResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    //회원가입
    @PostMapping("/join")
    public ResponseEntity<Map<String, Object>> join(@Validated @RequestBody JoinRequest request){

       Map<String, Object> response = new HashMap<>();
        try{
            Users joinedUser = userService.join(request);
            response.put("message", "User successfully registered");
            response.put("role", "MEMBER");
            response.put("userId", joinedUser.getUserId());
            return ResponseEntity.ok(response);
        }catch (DuplicateUserIdException e){//유저 아이디 중복 메시지처리
            response.put("error", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(response);
        }catch (Exception e) {
            // 기타 예외 처리
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    //세션확인하기
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> findLogedInUser(@SessionAttribute(name="loginUser",required=false) String userId){
        Map<String, Object> response = new HashMap<>();
       // System.out.println(logedMember.getUserName());
        if(userId == null){
            System.out.println("없는디");
            return ResponseEntity.ok(response);
        }
        System.out.println("있다 : " + userId);
        response.put("user", userId);
        return ResponseEntity.ok(response);
    }

    //로그인
    @PostMapping("/session-login")
    public ResponseEntity<Map<String ,Object>> sessionLogin(@Valid @ModelAttribute LoginRequest loginRequest, HttpServletRequest req){
        Users user = userService.login(loginRequest);
        Map<String, Object> response = new HashMap<>();
        //아이디, 비밀번호 확인하여 결괏값 반환(에러메시지)
        if(user == null){
            response.put("error", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }

        // 로그인 성공 => 세션 생성
        req.getSession().invalidate();
        HttpSession session = req.getSession(true);
        session.setAttribute("loginUser", user.getUserId());
        session.setMaxInactiveInterval(1800); //세션 유효 기간 : 30분 : 1800

        //System.out.println("sessionId : " + session.getId());
        response.put("success", "sessionLogin successed");
        return ResponseEntity.ok(response);
    }

    //로그아웃
    @GetMapping("/session-logout")
    public ResponseEntity<Map<String,Object>> sessionLogout(HttpSession session) throws Exception{
       Map<String, Object> response = new HashMap<>();
       try{
           response.put("success", "sessionLogout successed");
           return ResponseEntity.ok(response);
       }catch(Exception e){
           response.put("error", e.getMessage());
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
       }
    }

    //회원탈퇴
    @Transactional
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable String  id){
        userService.deletebyUserId(id);
    }

    //아이디 찾기
    @GetMapping("/find-id")
    public ResponseEntity<Map<String, Object>> findMyId(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();

        Users userFound = userService.findUserByEmail(email);
        if (userFound != null) {
            response.put("message", "아이디가 성공적으로 조회되었습니다.");
            response.put("userId", userFound.getUserId());
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "해당 이메일로 등록된 계정이 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    //비밀번호 찾기-이메일인증은 추후 추가여부 결정
    @PostMapping("/find-pw")
    public ResponseEntity<Map<String, Object>> findMyPw(@RequestParam String userId) {
        Map<String, Object> response = new HashMap<>();
        Users userFound = userService.findByUserId(userId);
        //TODO : 이메일 인증 후, 랜덤 코드(새비밀번호)생성
        if (userFound != null) {
            response.put("message", "비밀번호가 성공적으로 조회되었습니다.");
            response.put("userId", userFound.getUserPw());
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "해당 아이디로 등록된 계정이 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

    }

    //비밀번호 수정
    @PostMapping("/change-pw")
    public void changePw(@RequestBody String userId) {

        
    }

    //이메일 보내기
    @PostMapping("/send-email")
    public ResponseEntity<ResponseDto> sendEmail(@RequestBody @Validated EmailRequest request){
        emailService.sendEmail(request);
        log.info("sendMail code : {}, message : {}", HttpStatus.OK, HttpStatus.OK.getReasonPhrase());
        ResponseDto responseDto = ResponseDto.of(HttpStatus.OK, HttpStatus.OK.getReasonPhrase());

        // ResponseEntity로 반환
        return ResponseEntity.ok(responseDto);
    }


}
