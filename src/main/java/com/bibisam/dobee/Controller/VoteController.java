package com.bibisam.dobee.Controller;

import com.bibisam.dobee.DTO.Vote.VoteRequestDTO;
import com.bibisam.dobee.Service.AssociationService;
import com.bibisam.dobee.Service.UserService;
import com.bibisam.dobee.Service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vote")
public class VoteController {

    private final VoteService voteService;

    private final AssociationService associationService;

    private final UserService userService;

    // TODO : 투표 생성
    @PostMapping("/create-vote")
    public void createVote(@Validated @RequestBody VoteRequestDTO requestDTO
                                                  ) {



    }

    //투표하기
    @PostMapping("/voting/{voteId}")
    public void participateVote(@PathVariable Integer voteId) {

    }



    @GetMapping("/quorum")
    public void calculatequroum(){

    }

    // TODO : 투표 결과 조회
    @GetMapping("/result")
    public void getResult(){

    }
    // TODO : 남은 시간 조회하는 api
    @GetMapping("/time/left/{id}")
    public void timeLeft(@PathVariable Integer id){

    }



}
