package com.bibisam.dobee.Controller;

import com.bibisam.dobee.DTO.Association.AssociationJoinRequest;
import com.bibisam.dobee.DTO.Association.AssociationRequest;
import com.bibisam.dobee.Entity.Association;
import com.bibisam.dobee.Entity.Enum.AssociationStatus;
import com.bibisam.dobee.Entity.Enum.UserStatus;
import com.bibisam.dobee.Entity.Users;
import com.bibisam.dobee.Service.AssociationService;
import com.bibisam.dobee.Service.AuthService;
import com.bibisam.dobee.Service.UserService;
import com.bibisam.dobee.Service.VoteService;
import jakarta.persistence.EntityNotFoundException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/association")
public class AssociationController {

    @Autowired
    private AssociationService associationService;

    @Autowired
    VoteService voteService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;


    @PostMapping("/new/association")
    public ResponseEntity<Map<String, Object>> newAssociation(
            @Validated @RequestBody AssociationRequest request,
            HttpServletRequest httpRequest) {
        Map<String, Object> response = new HashMap<>();

        // 쿠키 받아오기
        Cookie[] cookies = httpRequest.getCookies();
        if (cookies == null || cookies.length == 0) {
            response.put("error", "No cookies found. Please log in first.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        String sessionIdFromCookie = null;
        for (Cookie cookie : cookies) {
            if ("JSESSIONID".equals(cookie.getName())) { // JSESSIONID는 기본 세션 쿠키 이름
                sessionIdFromCookie = cookie.getValue();
                break;
            }
        }

        if (sessionIdFromCookie == null || !sessionIdFromCookie.equals(httpRequest.getSession().getId())) {
            response.put("error", "Invalid session. Please log in again.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        // 세션에서 로그인 정보 확인
        Object loginUser = httpRequest.getSession().getAttribute("loginUser");
        if (loginUser == null) {
            response.put("error", "User not logged in. Please log in first.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        try {
            // 조합 생성 로직 수행
            Association association = associationService.createAssociation(request);

            // 추가 작업 (초기값 설정)
            association.setHeadId(null);
            association.setStatus(AssociationStatus.PENDING);

            //TODO : status 값이 null로 들어가는문제
            //TODO : 위도경도값저장하기.
            response.put("message", "Association created successfully.");
            response.put("association", association);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("error", "An error occurred while creating the association.");
            response.put("details", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //가입가능한 조합 리스트 확인
    @GetMapping("/check-to-join")
    public ResponseEntity<List<Association>> requestToJoin() {
        List<Association> list = associationService.getAllAssociations();
        return ResponseEntity.ok(list);
    }

    //가입여부확인 -> 첫화면
    @GetMapping("/is/member")
    public boolean isMember(@RequestParam String userId){
        return authService.isMember(userId);
    }

    // 1. 조합원이 가입 요청을 보내는 API
    @PostMapping("/request")
    public ResponseEntity<String> requestMembership(@RequestBody AssociationJoinRequest request) {
        Users user = userService.findByUserId(request.getUserId());
        user.setUserStatus(UserStatus.PENDING);
        userService.updateUser(user);
        return ResponseEntity.ok("join request success ");
    }

    //조합 가입 승인 api
    @GetMapping("/approve/join")
    public ResponseEntity<String> approveToJoin(@RequestParam int associationId, String userId) {
        Association association = associationService.findById(associationId);
        Users user = userService.findByUserId(userId);

        user.setUserStatus(UserStatus.AFTER_JOIN);
        user.setAssociation(association);
        //바뀐값 update
        userService.updateUser(user);
        return ResponseEntity.ok("approve to join request success ");
    }

    //TODO:조합 가입 거절
    @GetMapping("/decline/join")
    public void declineToJoin(@RequestParam int associationId, String userId) {
        //todo : 테이블에서 삭제(대기명단테이블)
    }
    //TODO : 조합의 가입 요청 리스트 확인
    @GetMapping("/request/list")
    public ResponseEntity<List<Users>> requestList(@RequestParam int associationId) {
        try {
            List<Users> userList = userService.findByAssociation(associationId);


            return ResponseEntity.ok(userList);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
