package com.bibisam.dobee.Service;

import com.bibisam.dobee.DTO.Vote.VoteRequestDTO;
import com.bibisam.dobee.Entity.Association;
import com.bibisam.dobee.Entity.Vote;
import com.bibisam.dobee.Entity.Vote_options;
import com.bibisam.dobee.Exceptions.Association.InvalidAssociationException;
import com.bibisam.dobee.Repository.VoteOptionRepository;
import com.bibisam.dobee.Repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteService {


    private final VoteRepository voteRepository;

    private final VoteOptionRepository voteOptionRepository;

    private final AssociationService associationService;
    //투표 생성
    public Vote ceateVote(VoteRequestDTO request) throws InvalidAssociationException {
        //TODO : 정족수 계산,  투표 종류 ( pre , post ) 하는거 -> 그리구 상태따라서 다른로직해야함..
        List<Vote_options> options = request.getOptions();
        Association votedAssociation = associationService.findById(request.getAssociationId());
        request.setAssociation(votedAssociation);
        Vote prevote = new Vote();
        prevote = request.toEntity();
        prevote.setTotalAgenda(options.size());
        Vote vote = voteRepository.save(prevote);
        // VoteOption 생성

        for (Vote_options optionText : request.getOptions()) {
            System.out.println("for문에서 한번저장했다..");
            Vote_options voteOption = new Vote_options();
            // vote_text 설정
            voteOption.setVote_text(optionText.getVote_text());
            // 연관관계 설정
            voteOption.setVote(vote);
            // 저장
            voteOptionRepository.save(voteOption);
        }

        return vote;
    }

    //정족수 달성여부확인
    public boolean checkQuorum(VoteRequestDTO request) {
        return false;
    }
}
