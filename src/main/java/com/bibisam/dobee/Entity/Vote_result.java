package com.bibisam.dobee.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class Vote_result {

    @Id
    private  int id;

    Timestamp vote_time;
    @ManyToOne
    @JoinColumn(name="vote_id")
    private Vote vote;

    @OneToOne
    @JoinColumn(name="option_id")
    private Vote_options vote_options;

    @OneToOne
    @JoinColumn(name="user_id")
    private Users users;
}
