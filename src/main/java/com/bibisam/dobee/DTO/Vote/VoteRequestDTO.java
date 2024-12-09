package com.bibisam.dobee.DTO.Vote;

import com.bibisam.dobee.Entity.Association;
import com.bibisam.dobee.Entity.Users;
import com.bibisam.dobee.Entity.Vote;
import com.bibisam.dobee.Entity.Enum.VoteStatus;
import com.bibisam.dobee.Entity.Enum.AssociationStatus;
import com.bibisam.dobee.Entity.Vote_options;
import com.bibisam.dobee.Repository.UserRepository;
import com.bibisam.dobee.Service.UserService;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

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

    @NotBlank(message = "투표 안건를 입력해주세요.") //String 만사용가능.
    String agenda;

    @NotNull
    @FutureOrPresent(message = "Start date must be in the future or present")
    LocalDateTime startDate;
    @NotNull
    @FutureOrPresent(message = "End date must be in the future or present")
    LocalDateTime endDate;
    AssociationStatus associationstatus;
    @NotNull(message = "Vote status is required")
    VoteStatus voteStatus;
    Association association;
    int associationId;
    Users users;
    private List<Vote_options> options;


    public Vote toEntity() {
        return Vote.builder()
                .title(this.title)
                .agenda(this.agenda)                     // DTO의 city 필드 매핑
                .startTime(this.startDate)             // DTO의 district 필드 매핑
                .endTime(this.endDate)       // DTO의 houseNumber 필드 매핑
                .options(this.options)
                .association(this.association)
                .users(this.users)
                .build();
    }
}
