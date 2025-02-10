package com.bibisam.dobee.Service;

import com.bibisam.dobee.DTO.Association.AssociationRequest;
import com.bibisam.dobee.Entity.Association;
import com.bibisam.dobee.Entity.Users;
import com.bibisam.dobee.Exceptions.Association.InvalidAssociationException;
import com.bibisam.dobee.Repository.AssociationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AssociationService {

    private final AssociationRepository associationRepository;

    public AssociationService(AssociationRepository associationRepository) {
        this.associationRepository = associationRepository;
    }


    public List<Association> getAllAssociations() {
        return associationRepository.findAll();
    }

    public Association createAssociation(AssociationRequest request) {
        return associationRepository.save(request.toEntity());
    }

    public Association update(Association association){
        return associationRepository.save(association);
    }

    public void deleteAll(){
        associationRepository.deleteAll();
    }

    //총인원 가져오기..
    public long getTotalCount() {
        return associationRepository.count();
    }

    //조합 상태 변경 : pending, created
    public void checkAndCreate(){
        //투표가 완료되었을 경우,
    }

    public Association getAssociationById(int associationId) {
        Optional<Association> asso =  associationRepository.findById(associationId);
        return asso.orElse(null);
    }

    public void addPendingUser(Association association, Users user) throws InvalidAssociationException {
        List<Users> pendingList = association.getPendingUsers();
        if (!pendingList.contains(user)) {
            pendingList.add(user);
        } else {
            throw new InvalidAssociationException("User is already in pending list");
        }
    }

    public void removePendingUser(Association association, Users user) throws InvalidAssociationException {
        List<Users> pendingList = association.getPendingUsers();
        if (pendingList.contains(user)) {
            pendingList.remove(user);
        } else {
            throw new InvalidAssociationException("User is not in pending list");
        }
    }

    public void removeUser(Association association, Users user) throws InvalidAssociationException {
        List<Users> userList = association.getUsers();
        if(userList.contains(user)){
            userList.remove(user);
        }else{
            throw new InvalidAssociationException("User is not in user list");
        }
    }

    public void addUser(Association association, Users user)
    throws InvalidAssociationException {
        List<Users> userList = association.getUsers();
        if (!userList.contains(user)) {
            userList.add(user);
        }else{
            throw new InvalidAssociationException("User is already in user list");
        }
    }
}
