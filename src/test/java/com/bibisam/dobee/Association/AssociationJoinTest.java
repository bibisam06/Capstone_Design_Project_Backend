package com.bibisam.dobee.Association;

import com.bibisam.dobee.Controller.AssociationController;
import com.bibisam.dobee.DTO.Association.AssociationJoinRequest;
import com.bibisam.dobee.Entity.Association;
import com.bibisam.dobee.Entity.Enum.UserStatus;
import com.bibisam.dobee.Entity.Users;
import com.bibisam.dobee.Exceptions.Association.InvalidAssociationException;
import com.bibisam.dobee.Service.AssociationService;
import com.bibisam.dobee.Service.AuthService;
import com.bibisam.dobee.Service.UserService;
import com.bibisam.dobee.Service.VoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AssociationController.class)
public class AssociationJoinTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AssociationService associationService;

    @MockBean
    private VoteService voteService;

    @MockBean
    private AuthService authService;

    @Mock
    private Users mockUser;

    @Test
    void testRequestMembership() throws Exception, InvalidAssociationException {
        // Given
        AssociationJoinRequest request = new AssociationJoinRequest("jade", 552);
        Users mockUser = new Users();  // Mock 객체 생성
        mockUser.setUserId("jade");    // Mock 데이터 설정
        mockUser.setUserStatus(UserStatus.BEFORE_JOIN);

        //then
        mockUser.setUserStatus(UserStatus.BEFORE_JOIN);

        Association mockAssociation = new Association();
        mockAssociation.setId(100);
        mockAssociation.setPendingUsers(new ArrayList<>());

        given(userService.findByUserId("jade")).willReturn(mockUser);
        given(associationService.findById(100)).willReturn(mockAssociation);

        //when
        mockMvc.perform(get("/api/users/1")
                        .header("Authorization", "Bearer token") //헤더에 authorization code추가..
                        .content("{\"userId\":\"jade\",\"associationId\":552}"))
                .andExpect(status().isOk());

    }

}
