package com.bibisam.dobee.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Vote_options {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;
    private  String vote_text;

    @ManyToOne
    @JoinColumn(name="vote_id")
    private Vote vote;
}
