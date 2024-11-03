package com.bibisam.dobee.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.mapping.ToOne;

@Getter
@Setter
@Entity
public class Quorum_calculation {

    @Id
    private  int id;

    private int total_voters;
    private boolean quorum_met;
    private int votes_cast;
    @OneToOne
    @JoinColumn(name="vote_id")
    private Vote vote;
}
