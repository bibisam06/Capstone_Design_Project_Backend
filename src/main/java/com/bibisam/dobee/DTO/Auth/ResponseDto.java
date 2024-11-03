package com.bibisam.dobee.DTO.Auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ResponseDto {
    private int status;
    private String message;

    public static ResponseDto of(HttpStatus status, String message) {
        ResponseDto response = new ResponseDto();
        response.setStatus(status.value());
        response.setMessage(message);
        return response;
    }
}
