package com.bibisam.dobee.Membership;


import com.bibisam.dobee.Entity.Users;
import com.bibisam.dobee.Entity.Vote;
import com.bibisam.dobee.Service.UserService;
import com.bibisam.dobee.Service.VoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Transactional
//@WebMvcTest(controllers = VoteController.class)
@SpringBootTest
public class VoteTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VoteService voteService;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private UserService userService;



    @Test
    @DisplayName("투표 생성 api 테스트")
    public void vote_test() throws Exception {

        //given
        Vote testVote = Vote.builder()
                .title("test vote")
                .agenda("for test")                     // DTO의 city 필드 매핑
                .startTime(LocalDateTime.of(2024, 12, 20, 10, 30, 0))        // DTO의 district 필드 매핑
                .endTime(LocalDateTime.of(2024, 12, 22, 20, 40, 0))     // DTO의 houseNumber 필드 매핑
                //.options("options")
                //.association("association")
               // .users("users")
                .build();
        // then
        String json = objectMapper.writeValueAsString(testVote);
        MvcResult voteResult = mockMvc.perform(post("/api/vote/create-vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();

        JSONObject response = new JSONObject(Integer.parseInt(voteResult.getResponse().getContentAsString()));
                assertEquals("0", response.get("result"));
                assertEquals("success", response.get("message"));

    }
    
}
