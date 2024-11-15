package com.bibisam.dobee.Service;

import com.bibisam.dobee.DTO.Association.AssociationRequest;
import com.bibisam.dobee.Entity.Association;
import com.bibisam.dobee.Repository.AssociationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AssociationService {

    @Autowired
    private AssociationRepository associationRepository;

    //모든 조합 조회 가능.
    public List<Association> getAllAssociations() {
        return associationRepository.findAll();
    }

    public Association createAssociation(AssociationRequest request) {
        return associationRepository.save(request.toEntity());
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

    public Optional<Association> getAssociationById(int associationId) {
        return associationRepository.findById(associationId);
    }

    public Association findById(int id){
        return associationRepository.findById(id).orElse(null);
    }
}
