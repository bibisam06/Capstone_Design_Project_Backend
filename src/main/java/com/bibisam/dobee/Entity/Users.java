package com.bibisam.dobee.Entity;

import com.bibisam.dobee.Entity.Enum.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users {


    @SequenceGenerator(
            name = "MEMBER_SEQ_GENERATOR",
            sequenceName = "MEMBER_SEQ",
            initialValue = 1,  // 초기값 1
            allocationSize = 1 // 매번 1씩 증가
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name="username")
    private String userName;

    @Column(name="user_id")
    private String userId;

    @Column(name="user_pw")
    private String userPw;

    @Column(name="role", nullable = false)
    @Builder.Default
    private String role = "MEMBER"; // 회원 - 조합장 - 가입 안한 사람(3개로)

    private String email;
    //false or true
    private Boolean isHead;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserStatus userStatus = UserStatus.BEFORE_JOIN; //Default 값 - before join

    @Column(name="phone_number")
    String phoneNumber;

    //jpa
    @OneToMany(mappedBy = "users")
    private final List<Vote> usersList = new ArrayList<Vote>();

    @OneToMany(mappedBy = "users")
    private final List<User_myvote> voteList = new ArrayList<User_myvote>();

    @ManyToOne
    @JoinColumn(name = "association_id")  // UnionTable과의 외래 키
    private Association association;


}
