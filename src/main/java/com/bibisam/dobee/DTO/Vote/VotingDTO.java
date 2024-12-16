package com.bibisam.dobee.DTO.Vote;

import com.bibisam.dobee.Entity.Vote;
import com.bibisam.dobee.Entity.Vote_options;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VotingDTO {

    private int association_id;
    private int vote_option_id;
    private int vote_id;

}
