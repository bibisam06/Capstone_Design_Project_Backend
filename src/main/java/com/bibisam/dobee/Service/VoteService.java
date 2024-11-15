package com.bibisam.dobee.Service;

import com.bibisam.dobee.DTO.Vote.VoteRequestDTO;
import com.bibisam.dobee.Entity.Vote;
import com.bibisam.dobee.Entity.Vote_options;
import com.bibisam.dobee.Repository.VoteOptionRepository;
import com.bibisam.dobee.Repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteService {

    @Autowired
    private final VoteRepository voteRepository;
    @Autowired
    private final VoteOptionRepository voteOptionRepository;

    //투표 생성
    public Vote ceateVote(VoteRequestDTO request) {

        //DTO -> ENTITY
        Vote vote = voteRepository.save(request.toEntity());

        // VoteOption 생성
        for (Vote_options optionText : request.getOptions()) {
            Vote_options voteOption = new Vote_options();
            voteOption.setVote(vote);
            voteOptionRepository.save(voteOption);
        }

       return voteRepository.save(vote);
    }

    //정족수 달성여부확인
    public boolean checkQuorum(VoteRequestDTO request) {
        return false;
    }
}
