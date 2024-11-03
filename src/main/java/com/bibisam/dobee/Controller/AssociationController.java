package com.bibisam.dobee.Controller;

import com.bibisam.dobee.DTO.Association.AssociationRequest;
import com.bibisam.dobee.Entity.Association;
import com.bibisam.dobee.Entity.Enum.AssociationStatus;
import com.bibisam.dobee.Entity.Enum.UserStatus;
import com.bibisam.dobee.Entity.Users;
import com.bibisam.dobee.Repository.UserRepository;
import com.bibisam.dobee.Repository.VoteRepository;
import com.bibisam.dobee.Service.AssociationService;
import com.bibisam.dobee.Service.AuthService;
import com.bibisam.dobee.Service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/association")
public class AssociationController {

    @Autowired
    private AssociationService associationService;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;


    //조합생성 api
    @PostMapping("/new-association")
    public ResponseEntity<Map<String, Object>> newAssociation(@Validated @RequestBody AssociationRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 서비스 계층을 통해 조합 생성
            Association association = associationService.createAssociation(request);
            //초기엔 조합장 null로 설정해둔뒤, 투표하고나서 결정
            association.setHeadId(null);
            association.setStatus(AssociationStatus.PENDING);
            // 성공 시 응답 반환
            response.put("association address", request.toString());
            response.put("조합이 성공적으로 생성되었습니다.", association);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            // 오류 발생 시 예외 메시지와 함께 Bad Request 반환
            response.put("error occured while creating association", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    //TODO: 신청 가능한 조합리스트 가져오기.
    @GetMapping("/check-to-join")
    public ResponseEntity<List<Association>> requestToJoin() {
        List<Association> list = associationService.getAllAssociations();

        return ResponseEntity.ok(list);
    }

    //가입여부확인 -> 첫화면 어캐나올지 ..
    @GetMapping("/is/member")
    public boolean isMember(@RequestParam String userId){
        return authService.isMember(userId);
    }

    // 1. 조합원이 가입 요청을 보내는 API
    @PostMapping("/request")
    public ResponseEntity<String> requestMembership(@RequestParam String userId) {
        //TODO : 이미 가입되어있는 경우, 에러메세지 나오게
        Users user = userService.findByUserId(userId);
        user.setUserStatus(UserStatus.PENDING);
      return ResponseEntity.ok("join request success ");
    }

    //TODO:조합 가입 승인
    @GetMapping("/approve-to-join")
    public ResponseEntity<String> approveToJoin(@RequestParam int associationId, String userId) {
        Association association = associationService.findById(associationId);
        Users user = userService.findByUserId(userId);

        user.setUserStatus(UserStatus.AFTER_JOIN);
        user.setAssociation(association);

        return ResponseEntity.ok("approve to join request success ");
    }
    //TODO:조합 가입 거절
    @GetMapping("/decline-to-join")
    public void declineToJoin(@RequestParam int associationId, String userId) {

    }
    //TODO : 조합의 가입 요청 리스트 확인
    @GetMapping("/request/list")
    public ResponseEntity<List<Users>> requestList(int associationId) {
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
