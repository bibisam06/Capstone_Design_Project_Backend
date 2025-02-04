package com.bibisam.dobee.Entity;

import com.bibisam.dobee.Entity.Enum.AssociationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Association {

    @SequenceGenerator(
            name = "ASSO_SEQ_GEN",         // 시퀀스 생성기 이름
            sequenceName = "ASSO_SEQ",     // 실제 DB에 저장될 시퀀스 이름
            initialValue = 1,              // 초기값 1
            allocationSize = 1
    )


    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ASSO_GEN")
    private int id;

    @Column(name="name")
    private String name;

    @Column(nullable = false)
    private String apartmentName;

    //주소
    @Column(name="city", nullable = false)
    private String city;
    @Column(name="district", nullable = false)
    private String district;
    @Column(name="house_number", nullable = false)
    private String houseNumber;
    @Column(name = "head_id", nullable = true)  // 실제 외래 키 컬럼
    private Integer headId;

    //조합 상태
    @Enumerated(EnumType.STRING)
    private AssociationStatus status = AssociationStatus.PENDING; //초기화

    //위도, 경도
    @Column(precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(precision = 9, scale = 6)
    private BigDecimal latitude;

    //jpa
    //가입자 목록
    @OneToMany(mappedBy = "association")
    private List<Users> users;

    //가입 승인 대기자 목록
    @OneToMany(mappedBy = "association")
    private List<Users> pendingUsers;
    //조합 생성 시간
    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    //조합장..
    @OneToOne
    @JoinColumn(name = "head_id", insertable = false, updatable = false)  // 중복된 컬럼에 대해 읽기 전용으로 설정
    private Users chairperson;


}
