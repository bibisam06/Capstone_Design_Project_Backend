package com.bibisam.dobee.Controller;

import com.bibisam.dobee.DTO.Association.AssociationJoinRequest;
import com.bibisam.dobee.DTO.Association.AssociationRequest;
import com.bibisam.dobee.DTO.Auth.ResponseDto;
import com.bibisam.dobee.Entity.Association;
import com.bibisam.dobee.Entity.Enum.AssociationStatus;
import com.bibisam.dobee.Entity.Enum.UserStatus;
import com.bibisam.dobee.Entity.Users;
import com.bibisam.dobee.Exceptions.Association.InvalidAssociationException;
import com.bibisam.dobee.Service.AssociationService;
import com.bibisam.dobee.Service.AuthService;
import com.bibisam.dobee.Service.UserService;
import com.bibisam.dobee.Service.VoteService;
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


    @PostMapping("/new/association")
    public ResponseEntity<ResponseDto> newAssociation(
            @Valid @RequestBody AssociationRequest request) {

        try {
            Association association = associationService.createAssociation(request);
            association.setStatus(AssociationStatus.PENDING);
            associationService.update(association);
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

    @GetMapping("/is/member")
    public boolean isMember(@RequestParam String userId){
        return authService.isMember(userId);
    }


    @PostMapping("/request")
    public ResponseEntity<String> requestMembership(@RequestBody AssociationJoinRequest request) throws InvalidAssociationException {
        Users findUser = userService.findByUserId(request.getUserId());
        Association asso = associationService.getAssociationById(request.getAssociationId());

        userService.changeStatus(findUser, UserStatus.PENDING);
        findUser.setAssociation(asso);
        userService.updateUser(findUser);

        associationService.addPendingUser(asso, findUser);
        return ResponseEntity.ok("join request success ");
    }

    @GetMapping("/pendingUsers/{id}")
    public ResponseEntity<List<Users>> getPendingUsers(@PathVariable int id)
    {
        Association findAsso = associationService.getAssociationById(id);
        List<Users> pendingList = findAsso.getPendingUsers();

        if(pendingList.isEmpty()){
            return ResponseEntity.badRequest().body(null);
        }else{
            return ResponseEntity.ok(pendingList);
        }
    }

    @PatchMapping("/approve/join")
    public boolean approveToJoin(@RequestParam int associationId, String userId) throws InvalidAssociationException {
        Association findAsso = associationService.getAssociationById(associationId);
        Users user = userService.findByUserId(userId);

        userService.changeStatus(user, UserStatus.AFTER_JOIN);
        associationService.removePendingUser(findAsso, user);
        associationService.addUser(findAsso, user);
        findAsso.getUsers().add(user);

        return true;
    }

    @PatchMapping("/decline/join")
    public boolean declineToJoin(@RequestParam int associationId, String userId) throws InvalidAssociationException {
        Association findAsso = associationService.getAssociationById(associationId);
        Users user = userService.findByUserId(userId);

        userService.changeStatus(user, UserStatus.BEFORE_JOIN);
        associationService.removePendingUser(findAsso, user);
        user.setAssociation(null);
        userService.updateUser(user);

        return true;
    }

    @GetMapping("/request/list")
    public ResponseEntity<List<Users>> requestList(@RequestParam int associationId) {
       Association association = associationService.getAssociationById(associationId);
       List<Users> assoList = association.getPendingUsers();
       return ResponseEntity.ok(assoList);
    }

    @DeleteMapping("/signout")
    public boolean signOut(@RequestParam int associationId, String userId) throws InvalidAssociationException {
        Users user = userService.findByUserId(userId);
        Association findAsso = associationService.getAssociationById(associationId);

        associationService.removeUser(findAsso, user);
        user.setAssociation(null);
        return true;
    }
}
