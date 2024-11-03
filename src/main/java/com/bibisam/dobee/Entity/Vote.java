package com.bibisam.dobee.Entity;

import com.bibisam.dobee.Entity.Enum.VoteStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Vote {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
   // int created_by;
    @Column(name="start_time", nullable=false)
    private LocalDateTime startTime;
    @Column(name="end_time", nullable=false)
    private LocalDateTime endTime;
    private String title;
    private String agenda;
    private double quorum;
    @Column(name="voting_satus")
    private  boolean votingStatus;
    @Column(name="total_agenda")
    private int totalAgenda;

    @Enumerated(EnumType.STRING)
    private VoteStatus voteStatus;
 //jpa 연관관계
    @ManyToOne
    @JoinColumn(name="created_by")
    private Users users;

    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL)
    private List<Vote_options> options;

    @OneToMany(mappedBy = "vote")
    private List<Vote_result> results;

    @ManyToOne
    @JoinColumn(name="association_id")
    private Association association;

}
