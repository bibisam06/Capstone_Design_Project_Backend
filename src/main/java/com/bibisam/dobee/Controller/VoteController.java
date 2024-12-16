package com.bibisam.dobee.Controller;

import com.bibisam.dobee.DTO.Auth.ResponseDto;
import com.bibisam.dobee.DTO.Vote.VoteRequestDTO;
import com.bibisam.dobee.DTO.Vote.VotingDTO;
import com.bibisam.dobee.Entity.User_myvote;
import com.bibisam.dobee.Entity.Users;
import com.bibisam.dobee.Entity.Vote;
import com.bibisam.dobee.Exceptions.Association.InvalidAssociationException;
import com.bibisam.dobee.Service.AssociationService;
import com.bibisam.dobee.Service.UserService;
import com.bibisam.dobee.Service.VoteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vote")
public class VoteController {

    private final VoteService voteService;

    private final AssociationService associationService;

    private final UserService userService;

    // TODO : 투표 생성
    @PostMapping("/create-vote")
    public ResponseEntity<ResponseDto> createVote(@Validated @RequestBody VoteRequestDTO requestDTO,
                                                  HttpServletRequest request) {

//        //로그인확인
       HttpSession session = request.getSession(false);
        if(session == null) {

            return ResponseEntity.badRequest().body(new ResponseDto(400, "You are not logined"));
        }
        String loginUser = (String) session.getAttribute("loginUser");

        try{
            Users findUser = userService.findByUserId(loginUser);
            requestDTO.setUsers(findUser); //로그인된 유저를 vote 저장
            Vote vote = voteService.ceateVote(requestDTO);
            return ResponseEntity.ok(new ResponseDto(500, "Vote created successfully"));
        }catch(InvalidAssociationException e){
            return ResponseEntity.badRequest()
                    .body(new ResponseDto(700, "Threre is not a valid association with" + requestDTO.getAssociationId()));
        }
    }

    //TODO : 투표하기
    @GetMapping("/voting")
    public void voting(HttpServletRequest request, @RequestBody VotingDTO votingDTO){


    }

    //TODO : myVote
    @GetMapping("/myvote")
    public ResponseEntity<List<User_myvote>> myVote(HttpServletRequest request){

        //1 . 로그인 된 유저를 가져 와서 -> TODO : 없다면 에러 처리 하기..
        HttpSession session = request.getSession(false);
//        if(session == null) {
//
//            return ResponseEntity.badRequest().body(new ResponseDto(400, "You are not logined"));
//        }
        String loginUser = (String) session.getAttribute("loginUser");
        Users findUser = userService.findByUserId(loginUser);


        List<User_myvote> myVoteList = findUser.getVoteList();
        return ResponseEntity.ok(myVoteList);
    }

}
