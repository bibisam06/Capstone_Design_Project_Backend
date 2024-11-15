package com.bibisam.dobee.DTO.Vote;

import com.bibisam.dobee.Entity.Vote;
import com.bibisam.dobee.Entity.Vote_options;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VotingDTO {

    private int id; //
    private int association_id; //조합 아이디.
    private Vote_options voted_option;

}
