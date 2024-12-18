package com.bibisam.dobee.Repository;

import com.bibisam.dobee.Entity.Vote;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @NonNull
    Optional<Vote> findById(@NonNull Integer voteId);

}
