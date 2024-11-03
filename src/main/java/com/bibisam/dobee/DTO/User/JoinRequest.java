package com.bibisam.dobee.DTO.User;

import com.bibisam.dobee.Entity.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequest {

    private int id;
    private String userName;

    @NotBlank(message = "로그인 아이디가 비어있습니다.")
    private String userId;
    @NotBlank(message = "비밀번호가 비어있습니다.")
    private String userPw;
    @NotBlank(message = "이메일이 비어있습니다.")
    @Email(message = "유효한 이메일 주소를 입력해 주세요.")
    private String email;

    private String role;
    public JoinRequest(String userName, String userId, String userPw, String role) {
        this.userName = userName;
        this.userId = userId;
        this.userPw = userPw;
        this.role = role;
    }

    // DTO에서 엔티티 객체로 변환
    public Users toEntity(String encodedPassword) {
        return Users.builder()
                .userId(this.userId)
                .userName(this.userName)
                .userPw(encodedPassword)
                .email(this.email)
                .role(this.role)
                .build();
    }

}
