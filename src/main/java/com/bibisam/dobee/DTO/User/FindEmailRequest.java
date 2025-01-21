package com.bibisam.dobee.DTO.User;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindEmailRequest {
    @NotBlank(message = "값을 입력해주세요.")
    private String userId;

    @NotBlank(message = "값을 입력해주세요.")
    private String userName;

    @NotBlank(message = "이메일이 비어있습니다.")
    @Email(message = "유효한 이메일 주소를 입력해 주세요.")
    private String email;

}
