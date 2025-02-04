package com.bibisam.dobee.Service;

import com.bibisam.dobee.DTO.User.JoinRequest;
import com.bibisam.dobee.DTO.User.LoginRequest;
import com.bibisam.dobee.Entity.Association;
import com.bibisam.dobee.Entity.AuthenticationToken;
import com.bibisam.dobee.Entity.Enum.UserStatus;
import com.bibisam.dobee.Entity.Users;
import com.bibisam.dobee.Exceptions.User.DuplicateUserIdException;
import com.bibisam.dobee.Repository.AssociationRepository;
import com.bibisam.dobee.Repository.RedisRepository;
import com.bibisam.dobee.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final AssociationRepository associationRepository;

    private final RedisRepository redisRepository;
    //회원 가입
    @Transactional
    public Users join(JoinRequest request) {
        if (checkUserIdDuplicate(request.getUserId())){
            throw new DuplicateUserIdException(400, "not avaliable : " + request.getUserId());
        }

        return userRepository.save(request.toEntity(encoder.encode(request.getUserPw())));
    }

    public void updateUser(Users user) {
        userRepository.save(user);
    }

    public void save(Users users){
        userRepository.save(users);
    }
    @Transactional
    public Users login(LoginRequest req) {
        Optional<Users> optionalUser = userRepository.findByUserId(req.getUserId());
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        Users user = optionalUser.get();
        if (!encoder.matches(req.getUserPw(), user.getUserPw())) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return user;
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

    public boolean validateToken(String uid, String inputCode) {
        AuthenticationToken token = redisRepository.findById(uid).orElse(null);
        if (token == null) {
            return false;
        }
        return token.getTokenValue().equals(inputCode);
    }

    public void changeStatus(Users users, UserStatus status){
        users.setUserStatus(status);
        userRepository.save(users);
    }
}
