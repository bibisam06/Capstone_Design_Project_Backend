package com.bibisam.dobee.Controller;

import com.bibisam.dobee.Entity.Association;
import com.bibisam.dobee.Service.AssociationService;
import com.bibisam.dobee.Service.AuthService;
import com.bibisam.dobee.Service.UserService;
import com.bibisam.dobee.Service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/check")
public class CheckController {
    private final AssociationService associationService;

    private final VoteService voteService;

    private final AuthService authService;

    private final UserService userService;

    @GetMapping("/check-to-join")
    public ResponseEntity<List<Association>> requestToJoin() {


        List<Association> list = associationService.getAllAssociations();
        return ResponseEntity.ok(list);
    }
}
