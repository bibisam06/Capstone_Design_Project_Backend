package com.bibisam.dobee.Controller;

import com.bibisam.dobee.DTO.Association.AssociationJoinRequest;
import com.bibisam.dobee.DTO.Association.AssociationRequest;
import com.bibisam.dobee.DTO.Auth.ResponseDto;
import com.bibisam.dobee.Entity.Association;
import com.bibisam.dobee.Entity.Enum.AssociationStatus;
import com.bibisam.dobee.Entity.Enum.UserStatus;
import com.bibisam.dobee.Entity.Users;
import com.bibisam.dobee.Exceptions.Association.InvalidAssociationException;
import com.bibisam.dobee.Repository.UserRepository;
import com.bibisam.dobee.Service.AssociationService;
import com.bibisam.dobee.Service.AuthService;
import com.bibisam.dobee.Service.UserService;
import com.bibisam.dobee.Service.VoteService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/association")
public class AssociationController {

    private final AssociationService associationService;

    private final VoteService voteService;

    private final AuthService authService;

    private final UserService userService;
    private final UserRepository userRepository;


    @PostMapping("/new/association")
    public ResponseEntity<ResponseDto> newAssociation(
            @Valid @RequestBody AssociationRequest request) {

        try {
            // 조합 생성 로직 수행
            Association association = associationService.createAssociation(request);

            // 추가 작업 (초기값 설정)
            association.setHeadId(null);
            association.setStatus(AssociationStatus.PENDING);
            // 위도/경도 값 저장 부분 (TODO로 남겨둠)
            // 예: association.setLatitude(request.getLatitude());
            // 예: association.setLongitude(request.getLongitude());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDto(201, "Association created successfully"+ association));
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(500, e.getMessage()));
        }
    }
    
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


    @PostMapping("/request")
    public ResponseEntity<String> requestMembership(@RequestBody AssociationJoinRequest request) throws InvalidAssociationException {
        Users findUser = userService.findByUserId(request.getUserId());
        Association asso = associationService.getAssociationOrThrow(request.getAssociationId());

        userService.changeStatus(findUser, UserStatus.PENDING);
        findUser.setAssociation(asso);
        userService.updateUser(findUser);

        associationService.addPendingUser(asso, findUser);
        return ResponseEntity.ok("join request success ");
    }

    @GetMapping("/pendingUsers/{id}")
    public ResponseEntity<List<Users>> getPendingUsers(@PathVariable int id) throws InvalidAssociationException
    {
        Association findAsso = associationService.getAssociationOrThrow(id);
        List<Users> pendingList = findAsso.getPendingUsers();

        if(pendingList.isEmpty()){
            return ResponseEntity.badRequest().body(null);
        }else{
            return ResponseEntity.ok(pendingList);
        }
    }

    @GetMapping("/approve/join")
    public boolean approveToJoin(@RequestParam int associationId, String userId) throws InvalidAssociationException {
        Association findAsso = associationService.getAssociationOrThrow(associationId);
        Users user = userService.findByUserId(userId);

        userService.changeStatus(user, UserStatus.AFTER_JOIN);
        associationService.removePendingUser(findAsso, user);
        findAsso.getUsers().add(user);

        return true;
    }

    @GetMapping("/decline/join")
    public boolean declineToJoin(@RequestParam int associationId, String userId) throws InvalidAssociationException {
        Association findAsso = associationService.getAssociationOrThrow(associationId);
        Users user = userService.findByUserId(userId);

        userService.changeStatus(user, UserStatus.BEFORE_JOIN);
        associationService.removePendingUser(findAsso, user);
        user.setAssociation(null);
        userService.updateUser(user);

        return true;
    }

    //TODO : 4. 조합의 가입 요청 리스트 확인
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

    //TODO : 5. 조합 탈퇴
    @GetMapping("/delete/{id}")
    public void signOut(@PathVariable int id) {

    }
}
