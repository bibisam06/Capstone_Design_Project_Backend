package com.bibisam.dobee.Membership;

import com.bibisam.dobee.DTO.User.FindEmailRequest;
import com.bibisam.dobee.Entity.Users;
import com.bibisam.dobee.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
   private UserService userService;



    @DisplayName("임시비밀번호 테스트")
    @Test
    public void createCodeTest() throws Exception {
        //given
        FindEmailRequest request = new FindEmailRequest("jade", "Example User", "user@example.com");
        String json = objectMapper.writeValueAsString(request);
        Users users = userService.findByUserId("jade");
        //when
        MvcResult result = mockMvc.perform(patch("/api/user/find-pw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();
        //then
        JSONObject response = new JSONObject(Integer.parseInt(result.getResponse().getContentAsString()));

        assertEquals(users.getUserPw(), response.get("message"));
    }
}
