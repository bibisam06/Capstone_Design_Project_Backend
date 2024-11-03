package com.bibisam.dobee.DTO.Association;

import com.bibisam.dobee.Entity.Association;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AssociationRequest {

    @NotBlank(message = "조합이름을 입력해주세요.")
    private String name;
    @NotBlank(message = "아파트 주소가 비어있습니다.")
    private String city;
    @NotBlank(message = "아파트 주소가 비어있습니다.")
    private String district;
    @NotBlank(message = "아파트 주소가 비어있습니다.")
    private String houseNumber;
    @NotBlank(message = "아파트 이름이 비어있습니다.")
    private String apartmentName;


    //주소 toString
    @Override
    public String toString() {
        return city+" "+district+" "+houseNumber+" "+apartmentName;
    }

    @Builder
    public AssociationRequest(String name,String city, String district, String houseNumber, String apartmentName) {
        this.name = name;
        this.city = city;
        this.district = district;
        this.houseNumber = houseNumber;
        this.apartmentName = apartmentName;

    }

    //DTO에서 엔티티 객체로 변환
    public Association toEntity() {
        return Association.builder()
                .name(this.name)
                .city(this.city)                     // DTO의 city 필드 매핑
                .district(this.district)             // DTO의 district 필드 매핑
                .houseNumber(this.houseNumber)       // DTO의 houseNumber 필드 매핑
                .apartmentName(this.apartmentName)   // DTO의 apartmentName 필드 매핑
                .build();
    }

}
