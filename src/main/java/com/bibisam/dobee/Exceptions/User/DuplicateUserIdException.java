package com.bibisam.dobee.Exceptions.User;

import lombok.Getter;

@Getter
public class DuplicateUserIdException extends RuntimeException{
    private final int statusCode;
    private final String message;


    // 기본 생성자 (상태 코드 기본값 설정 가능)
    public DuplicateUserIdException(String message) {
        this(400, "이미 존재하는 아이디입니다."); // 기본 상태 코드를 400으로 설정
    }

    public DuplicateUserIdException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }


}
