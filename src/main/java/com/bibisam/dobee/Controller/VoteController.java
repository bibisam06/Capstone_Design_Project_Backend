package com.bibisam.dobee.Controller;

import com.bibisam.dobee.DTO.Vote.VoteRequestDTO;
import com.bibisam.dobee.Entity.Vote;
import com.bibisam.dobee.Entity.Enum.VoteStatus;
import com.bibisam.dobee.Service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vote")
public class VoteController {

    @Autowired
    private VoteService voteService;

    //TODO : 투표 생성
    @PostMapping("/create-vote")
    public ResponseEntity<Map<String,String>> createVote(@RequestHeader("Allow")VoteStatus voteStatus, @Validated @RequestBody VoteRequestDTO requestDTO) {
        Map<String, String> response = new HashMap<>();
        // if pre_creation -> 동의 투표 진행
        if (voteStatus == VoteStatus.PRE_CREATION) {
            // 동의 투표 생성 로직
            Vote vote = voteService.ceateVote(requestDTO);
            response.put("status", "동의 투표 생성됨");
            // 동의 투표 처리 로직 추가
        }
        // else -> 일반 투표 생성
        else if (voteStatus == VoteStatus.POST_CREATION) {
            // 일반 투표 생성 로직
            voteService.ceateVote(requestDTO);
            response.put("status", "일반 투표 생성됨");
            // 일반 투표 처리 로직 추가
        } else {
            // 잘못된 상태일 경우 예외 처리
            response.put("error", "유효하지 않은 투표 상태");
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }
}
