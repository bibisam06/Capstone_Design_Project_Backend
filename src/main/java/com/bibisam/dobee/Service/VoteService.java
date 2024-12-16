package com.bibisam.dobee.Service;

import com.bibisam.dobee.DTO.Vote.VoteRequestDTO;
import com.bibisam.dobee.Entity.Association;
import com.bibisam.dobee.Entity.Enum.VoteStatus;
import com.bibisam.dobee.Entity.Vote;
import com.bibisam.dobee.Entity.Vote_options;
import com.bibisam.dobee.Exceptions.Association.InvalidAssociationException;
import com.bibisam.dobee.Repository.VoteOptionRepository;
import com.bibisam.dobee.Repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
        //Vote 생성
        //TODO : association관련정보 null로들어가는거 수정해야함..
        Association votedAssociation = associationService.findById(request.getAssociationId());
        request.setAssociation(votedAssociation);
        Vote prevote = new Vote();
        prevote = request.toEntity();

        Vote vote = voteRepository.save(prevote);
        // VoteOption 생성
        List<Vote_options> options = request.getOptions();

        for (Vote_options optionText : request.getOptions()) {
            Vote_options voteOption = new Vote_options();
            System.out.println(voteOption);
            voteOption.setVote(vote);
            voteOptionRepository.save(voteOption);
        }

        return vote;
    }

    //정족수 달성여부확인
    public boolean checkQuorum(VoteRequestDTO request) {
        return false;
    }
}
