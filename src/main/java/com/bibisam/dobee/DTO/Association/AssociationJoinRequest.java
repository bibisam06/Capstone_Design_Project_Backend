package com.bibisam.dobee.DTO.Association;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociationJoinRequest {

    private String userId;
    private int associationId;

    public AssociationJoinRequest(String userId, int associationId){
        this.userId = userId;
        this.associationId = associationId;
    }

}
