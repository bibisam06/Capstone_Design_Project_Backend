package com.bibisam.dobee.Service;

import com.bibisam.dobee.DTO.User.JoinRequest;
import com.bibisam.dobee.DTO.User.LoginRequest;
import com.bibisam.dobee.Entity.Association;
import com.bibisam.dobee.Entity.Enum.UserStatus;
import com.bibisam.dobee.Entity.Users;
import com.bibisam.dobee.Exceptions.User.DuplicateUserIdException;
import com.bibisam.dobee.Repository.AssociationRepository;
import com.bibisam.dobee.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final AssociationRepository associationRepository;


    //회원 가입
    @Transactional
    public Users join(JoinRequest request) {
        if (checkUserIdDuplicate(request.getUserId())){
            throw new DuplicateUserIdException(400, "not avaliable : " + request.getUserId());
        }

        return userRepository.save(request.toEntity(encoder.encode(request.getUserPw())));
    }

    public Users updateUser(Users user) {
        return userRepository.save(user);
    }

    //로그인
    public Users login(LoginRequest req) {
        Optional<Users> optionalUser = userRepository.findByUserId(req.getUserId());
        System.out.println("reqId : " + req.getUserId() + " reqPw : " + req.getUserPw());
        System.out.println("OptionalUser : " + optionalUser.isPresent());
        // loginId와 일치하는 User가 없으면 null return
        if(optionalUser.isEmpty()) {
            return null;
        }

        Users user = optionalUser.get();
        System.out.println("reqId : " + user.getUserId() + "reqPw : " + user.getUserPw());
        // 찾아온 User의 password와 입력된 password가 다르면 null return
        if(encoder.matches(req.getUserPw(), user.getUserPw())) {
            return user;
        }
        return null;
    }


    //유저 아이디 중복 확인
    public boolean checkUserExists(Integer id) {
        return userRepository.existsById(id);
    }

    //userId중복 확인
    public boolean checkUserIdDuplicate(String userId) {
        return userRepository.existsByUserId(userId);
    }

    //모든유저삭제
    public void deleteallUser(){
        userRepository.deleteAll();
        //모든 유저 삭제시 초기화
        userRepository.initializeSequence();
    }

    //탈퇴
    public void deletebyUserId(String id) {
        userRepository.deleteByUserId(id);
    }

    public Users findByUserId(String userId) {
        Optional<Users> user = userRepository.findByUserId(userId);
        return user.orElse(null);
    }

    public Users findUserByEmail(String email) {
        Optional<Users> usersOptional= userRepository.findByEmail(email);
        System.out.println("email = " + email);
        System.out.println("유저존재여부 = " + usersOptional.isPresent());
        return usersOptional.orElse(null);
    }

    public Users findByUserName(String userName){
        Optional<Users> user = userRepository.findByUserName(userName);
        return user.orElse(null);
    }

    public List<Users> findByAssociation(int associationId){
        Optional<Association> optionalAssociation = associationRepository.findById(associationId);
        if (optionalAssociation.isPresent()) {
            Association association = optionalAssociation.get();  // Optional에서 실제 Association 객체를 꺼냄
            return userRepository.findByAssociationAndUserStatus(association, UserStatus.PENDING);
        } else {
            throw new EntityNotFoundException("Association not found with id " + associationId);
        }
    }


}
