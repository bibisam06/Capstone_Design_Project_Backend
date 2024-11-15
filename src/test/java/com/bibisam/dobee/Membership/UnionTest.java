package com.bibisam.dobee.Membership;

import com.bibisam.dobee.DTO.Association.AssociationRequest;
import com.bibisam.dobee.Entity.Association;
import com.bibisam.dobee.Service.AssociationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
public class UnionTest {

    @Autowired
    private AssociationService associationService;
    @AfterEach
    @Rollback  // 트랜잭션 롤백
    public void tearDown() {
        // 모든 사용자 삭제
        associationService.deleteAll();
    }

    @Test
    @DisplayName("조합 생성 테스트")
    public void createUnion(){
        //given
        AssociationRequest request = new AssociationRequest();
        //then
        request.setCity("금정구");
        request.setDistrict("구서동");
        request.setName("가로세로협동조합");
        request.setApartmentName("구서 롯데캐슬");
        request.setHouseNumber("금강로 502번");

        //when
        Association association = associationService.createAssociation(request);
    }

    @Test
    @DisplayName("조합 가입 요청 테스트")
    public void createRequest(){

    }

    @Test
    @DisplayName("가입가능 조합 리스트 기능")
    public void checkList(){

    }
}
