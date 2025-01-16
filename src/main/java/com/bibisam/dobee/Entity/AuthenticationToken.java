package com.bibisam.dobee.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@RedisHash(value="AuthToken")
public class AuthenticationToken {

    @Id
    private String uid;

    @Indexed
    private Integer tokenValue; //인증용 랜덤코드

    @TimeToLive
    private Long expiration = 10L; //만료
}
