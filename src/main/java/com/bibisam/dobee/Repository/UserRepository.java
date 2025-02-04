package com.bibisam.dobee.Repository;

import com.bibisam.dobee.Entity.Association;
import com.bibisam.dobee.Entity.Enum.UserStatus;
import com.bibisam.dobee.Entity.Users;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    boolean existsById(@NonNull Integer loginId);
    void deleteAll();
    boolean existsByUserId(String userName);

    Optional<Users> findByUserId(String userId);
    Optional<Users> findByEmail(String eamil);
    Optional<Users> findByUserName(String userName);
    List<Users> findByAssociationAndUserStatus(Association association, UserStatus userStatus);
    @Modifying
    @Transactional
    @Query(value = "ALTER SEQUENCE MEMBER_SEQ RESTART WITH 1", nativeQuery = true)
    void initializeSequence();

    void deleteByUserId(String userId);

}
