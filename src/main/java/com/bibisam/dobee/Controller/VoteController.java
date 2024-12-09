package com.bibisam.dobee.Controller;

import com.bibisam.dobee.DTO.Vote.VoteCreateResponse;
import com.bibisam.dobee.DTO.Vote.VoteRequestDTO;
import com.bibisam.dobee.Entity.Association;
import com.bibisam.dobee.Entity.Users;
import com.bibisam.dobee.Entity.Vote;
import com.bibisam.dobee.Exceptions.Association.InvalidAssociationException;
import com.bibisam.dobee.Service.AssociationService;
import com.bibisam.dobee.Service.UserService;
import com.bibisam.dobee.Service.VoteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vote")
public class VoteController {

    @Autowired
    private VoteService voteService;
    @Autowired
    private AssociationService associationService;
    @Autowired
    private UserService userService;

    // TODO : 투표 생성
    @PostMapping("/create-vote")
    public ResponseEntity<VoteCreateResponse> createVote(@Validated @RequestBody VoteRequestDTO requestDTO,
                                                         HttpServletRequest request) {

        //로그인확인
        HttpSession session = request.getSession(false);
        if(session == null) {

            return ResponseEntity.badRequest().body(new VoteCreateResponse("You are not logined", false));
        }
        String loginUser = (String) session.getAttribute("loginUser");

        try{
            Association findAssociation = associationService.findById(requestDTO.getAssociationId());
            Users findUser = userService.findByUserId(loginUser);
            requestDTO.setUsers(findUser); //로그인된 유저를 vote 저장
            Vote vote = voteService.ceateVote(requestDTO);
            return ResponseEntity.ok(new VoteCreateResponse("seccess", true));
        }catch(InvalidAssociationException e){
            return ResponseEntity.badRequest().body(new VoteCreateResponse("failed by unknown issues..", false));
        }
    }

    //TODO : 투표하기
    @GetMapping("/voting")
    public void voting(){


    }

    //TODO : myVote
    @GetMapping("/myvote")
    public void myVote(){

    }

}
