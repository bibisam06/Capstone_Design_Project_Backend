package com.bibisam.dobee.Controller;

import com.bibisam.dobee.DTO.Auth.ResponseDto;
import com.bibisam.dobee.DTO.User.FindEmailRequest;
import com.bibisam.dobee.DTO.User.JoinRequest;
import com.bibisam.dobee.DTO.User.LoginRequest;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

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
