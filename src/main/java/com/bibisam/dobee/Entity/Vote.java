package com.bibisam.dobee.Entity;

import com.bibisam.dobee.Entity.Enum.VoteStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Column(name="start_time", nullable=false)
    private LocalDateTime startTime;

    @Column(name="end_time", nullable=false)
    private LocalDateTime endTime;

    private String title;

    private String agenda;

    private double quorum; //정족수 -> Association 총인원해서.. 3분의 2값 계산해서 넣어둘거임,, 나중에 유무효 판단할때,,

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private VoteStatus voteStatus;


    @ManyToMany
    private List<Users> voters; //표 참여자 목록 저장하는 배열..

    @Column(name = "total_users")
    private int totalUsers; // 총 투표 대상자 수

    @Column(name = "valid", nullable = true)
    private boolean valid; // 유효/무효 투표 여부 -> null 초기에 저장 되고, 실제 투표 종료 시 true or false 저장될 것..

//    @Column(name = "result", nullable = true)
//    private Vote_options options;

    //JPA MAPPING 연관 관계
    @ManyToOne
    @JoinColumn(name="created_by")
    private Users users;

    @OneToMany(mappedBy = "vote")
    private List<Vote_options> options;

    @Column(name="total_agenda")
    private int totalAgenda;

    @OneToMany(mappedBy = "vote")
    private List<Vote_result> results;

    @ManyToOne
    @JoinColumn(name="association")
    private Association association;

}
