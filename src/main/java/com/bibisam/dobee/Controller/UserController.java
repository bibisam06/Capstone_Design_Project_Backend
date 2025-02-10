//UserController
package com.bibisam.dobee.Controller;

import com.bibisam.dobee.DTO.Auth.ResponseDto;
import com.bibisam.dobee.DTO.User.FindEmailRequest;
import com.bibisam.dobee.DTO.User.JoinRequest;
import com.bibisam.dobee.DTO.User.LoginRequest;
import com.bibisam.dobee.DTO.User.MyPageResponse;
import com.bibisam.dobee.Entity.AuthenticationToken;
import com.bibisam.dobee.Entity.Users;
import com.bibisam.dobee.Exceptions.User.DuplicateUserIdException;
import com.bibisam.dobee.Repository.RedisRepository;
import com.bibisam.dobee.Service.EmailService;
import com.bibisam.dobee.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@Validated
@Slf4j
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    private final EmailService emailService;

    private final RedisRepository redisRepository;

    private final PasswordEncoder encoder;

    //회원가입
    @PostMapping("/join")
    public ResponseEntity<Map<String, Object>> join(@Valid @RequestBody JoinRequest request) {

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
        response.put("sessionId",request.getSession().getId());
        response.put("request", request.getSession().getAttribute("loginUser"));
        return ResponseEntity.ok(response);
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> sessionLogin(
            @Valid @ModelAttribute LoginRequest loginRequest, HttpServletRequest req) {
        try {
            Users user = userService.login(loginRequest);
            req.getSession().invalidate();
            HttpSession session = req.getSession(true);
            session.setAttribute("loginUser", user.getUserId());
            session.setMaxInactiveInterval(1800);

            return ResponseEntity.ok(new ResponseDto(200, "Login succeeded"+user.getUserId()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto(400, ex.getMessage()));
        }
    }
    //TODO : oAuth
    @GetMapping("/oAuth")
    public void oAuth(){

    }
    //로그아웃

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

    // TODO : 반환값 주기
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

    //비밀번호 찾기 - 임시 비밀번호 발급
    @PatchMapping("/find_pw")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody FindEmailRequest request) {
        Map<String, Object> response = new HashMap<>();

        Users userFound = userService.findByUserId(request.getUserId());
        if (userFound == null) {
            response.put("error", "해당 아이디로 등록된 계정이 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        String temporaryPassword = emailService.createCode(8);
        String hashedPassword = encoder.encode(temporaryPassword);
        userFound.setUserPw(hashedPassword);
        userService.save(userFound);

        response.put("message", "새로운 임시 비밀번호가 생성되었습니다.");
        response.put("temporaryPassword", temporaryPassword);
        return ResponseEntity.ok(response);
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

    //TODO : 정보바꾸기 - 마이페이지 일단 나오고 나서 할듯 ..
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
        //TODO  : 조합관련을 어캐 넘겨줄지? 이거 inner클래스 만들어서 할까 ?
        return ResponseEntity.ok(response);
    }

    //인증코드 전송
    @PostMapping("/send-verification")
    public String sendVerificationEmail(@RequestParam String email) {
        try {
            String verificationCode = emailService.createCode(6);

            //redisTemplate.opsForValue().set(email, verificationCode, 5, TimeUnit.MINUTES);
            AuthenticationToken newToken = new AuthenticationToken(email, verificationCode, 300L);
            redisRepository.save(newToken);

            String htmlContent = """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Email Verification</title>
                    </head>
                    <body>
                        <h1>Email Verification</h1>
                        <p>Your verification code is:</p>
                        <h2 style="color: blue;">%s</h2>
                        <p>Please enter this code in the application to verify your email address.</p>
                    </body>
                    </html>
                    """.formatted(verificationCode);

            // Send the email
            emailService.sendHtmlEmail(email, "Email Verification", htmlContent);

            return "Verification email sent successfully!";
        } catch (Exception e) {
            return "Failed to send verification email.";
        }
    }

    //인증코드 확인
    @GetMapping("/check-verification")
    public String checkVeri(@RequestParam String verificationCode, @RequestParam String email){

        final boolean isValid = userService.validateToken(email, verificationCode);
        if(isValid){
            return "success";
        }else{
            return "유효하지 않거나 만료된 인증코드입니다.";
        }

    }

}
