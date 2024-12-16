package com.bibisam.dobee.Service;

import com.bibisam.dobee.Entity.Users;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    // 사용자가 조합에 가입되어 있는지 확인
    public boolean isMember(String userId) {
        // 해당 사용자의 가입된 조합이 있는지 확인 (조합 정보 조회)
        System.out.println(userId);
        Users user = userService.findByUserId(userId);
        System.out.println("user = " + user);
        // 사용자의 조합이 존재하는지 확인 (null 체크)
        return user.getAssociation() != null;
    }
}
