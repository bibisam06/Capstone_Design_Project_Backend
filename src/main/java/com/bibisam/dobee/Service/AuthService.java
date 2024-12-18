package com.bibisam.dobee.Service;

import com.bibisam.dobee.Entity.Users;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public boolean isMember(String userId) {
        Users user = userService.findByUserId(userId);
        return user.getAssociation() != null;
    }
}
