package com.bibisam.dobee.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
public class User_myvote {

    @Id
    @GeneratedValue
    private  int id;

    Timestamp vote_time;

    @Column(name="result")
    private String result;
    
    //JPA 연관관계 매핑
    @ManyToOne
    @JoinColumn(name="user_id")
    private Users users;

    @OneToOne
    @JoinColumn(name="vote_id")
    private Vote vote;
    
}
