//UserController
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;



@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    //TODO : 전역에러, 커스텀에러 등 처리 ,어노테이션
    //회원가입
    @PostMapping("/join")
    public ResponseEntity<Map<String, Object>> join(@Validated @RequestBody JoinRequest request) {

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
    @GetMapping("/check/session")
    public ResponseEntity<Map<String, Object>> findLogedInUser(@SessionAttribute(name="loginUser",required=false) String userId, HttpServletRequest request){
        Map<String, Object> response = new HashMap<>();
        if(userId == null){ //userId 없는경우.
            System.out.println("noUserId");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        System.out.println(request.getSession().getAttribute("loginUser"));
        response.put("user", userId);
        response.put("request", request.getSession().getAttribute("loginUser"));
        return ResponseEntity.ok(response);
    }

    //로그인
    @PostMapping("/login")
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
        // 세션 생성하기 전에 기존의 세션을 파기
        HttpSession session = req.getSession(true);
        System.out.println("sessioncreated : "+ req.getSession().getId());
        session.setAttribute("loginUser", user.getUserId());
        session.setMaxInactiveInterval(1800); //세션 유효 기간 : 30분 : 1800

        //System.out.println("sessionId : " + session.getId());
        response.put("success", "sessionLogin successed");
        response.put("user", user.getUserId());
        return ResponseEntity.ok(response);
    }

    //로그아웃
    @GetMapping("/logout")
    public ResponseEntity<Map<String,Object>> sessionLogout(HttpSession session) throws Exception{
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

    //회원탈퇴
    @Transactional
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable String  id){
        userService.deletebyUserId(id);
    }

    //아이디 찾기
    @GetMapping("/find_id")
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
    @PostMapping("/find_pw")
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
    @PostMapping("/change_pw")
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

    //이메일 보내기
    @PostMapping("/send_email")
    public ResponseEntity<ResponseDto> sendEmail(@RequestBody @Validated EmailRequest request){
        emailService.sendEmail(request);
        log.info("sendMail code : {}, message : {}", HttpStatus.OK, HttpStatus.OK.getReasonPhrase());
        ResponseDto responseDto = ResponseDto.of(HttpStatus.OK, HttpStatus.OK.getReasonPhrase());


        return ResponseEntity.ok(responseDto);
    }


}
