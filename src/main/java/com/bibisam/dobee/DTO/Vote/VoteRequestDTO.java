package com.bibisam.dobee.DTO.Vote;

import com.bibisam.dobee.Entity.Vote;
import com.bibisam.dobee.Entity.Enum.VoteStatus;
import com.bibisam.dobee.Entity.Enum.AssociationStatus;
import com.bibisam.dobee.Entity.Vote_options;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class VoteRequestDTO {


    @NotBlank(message = "제목을 입력해주세요.")
    String title;

    @NotBlank(message = "투표 안건를 입력해주세요.")
    String agenda;

    LocalDateTime startDate;
    LocalDateTime endDate;
    AssociationStatus associationstatus;
    VoteStatus voteStatus;
    int associationId;

    private List<Vote_options> options;


    public Vote toEntity() {
        return Vote.builder()
                .title(this.title)
                .agenda(this.agenda)                     // DTO의 city 필드 매핑
                .startTime(this.startDate)             // DTO의 district 필드 매핑
                .endTime(this.endDate)       // DTO의 houseNumber 필드 매핑
                .options(this.options)
                .build();
    }

}
