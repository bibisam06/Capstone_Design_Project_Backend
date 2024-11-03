package com.bibisam.dobee.DTO.Auth;

import com.bibisam.dobee.Entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class EmailRequest {

    private String to; //보내는 메일 주소
    private String subject; //제목
    private String text; //내용

}
