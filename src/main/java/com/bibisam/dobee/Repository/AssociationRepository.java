package com.bibisam.dobee.Repository;

import com.bibisam.dobee.Entity.Association;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssociationRepository extends JpaRepository<Association, Integer> {

    @SuppressWarnings("unchecked")
    @NonNull
    Association save(@NonNull Association association);
    public void delete(@NonNull Association association);
    long count();

    @Override
    @NonNull
    Optional<Association> findById(@NonNull Integer AssociationId);
}
