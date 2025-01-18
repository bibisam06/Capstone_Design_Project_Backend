package com.bibisam.dobee.DTO.User;

import com.bibisam.dobee.Entity.Association;
import com.bibisam.dobee.Entity.Enum.UserStatus;
import com.bibisam.dobee.Entity.Vote;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyPageResponse {

    private String userName;
    private String userId;
    private String role;
    private String email;
    private String phoneNumber;
    private UserStatus userStatus;
    private List<Vote> myVoteList;
    private Association association;
}
