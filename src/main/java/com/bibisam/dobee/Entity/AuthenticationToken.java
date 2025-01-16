package com.bibisam.dobee.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@RedisHash(value="AuthToken", timeToLive = 300) //5분
public class AuthenticationToken {

    @Id
    private String uid;

    @Indexed
    private String tokenValue;

    @TimeToLive
    private Long expiration;
}
