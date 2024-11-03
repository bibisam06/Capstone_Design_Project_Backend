package com.bibisam.dobee.Entity;

import com.bibisam.dobee.Entity.Enum.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import org.antlr.v4.runtime.misc.NotNull;

import java.sql.Timestamp;
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

    @Column(name="role")
    private String role;

    private String email;

    private Boolean isHead;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

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
