package com.bibisam.dobee.DTO.Vote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VoteCreateResponse {
    private String message;
    private boolean success;
}
