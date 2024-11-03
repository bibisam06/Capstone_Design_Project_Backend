package com.bibisam.dobee.Repository;

import com.bibisam.dobee.Entity.Vote;
import com.bibisam.dobee.Entity.Vote_options;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteOptionRepository extends JpaRepository<Vote_options, Integer> {

   // Vote_options save(Vote_options vote_options);
}
