package com.bibisam.dobee.DTO.Auth;


import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class EmailRequest {

    @Email(message = "유효한 이메일 주소를 입력해 주세요.")
    private String to; //보내는 메일 주소
    private String subject; //제목
    private String text; //내용

}
