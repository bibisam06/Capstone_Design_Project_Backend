package com.bibisam.dobee.Service;

import com.bibisam.dobee.Entity.Vote_options;
import com.bibisam.dobee.Repository.VoteOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteOptionService {

    @Autowired
    VoteOptionRepository voteOptionRepository;

    public/*Vote_options*/ void createOptions(){

    }
}
