package com.bibisam.dobee.Repository;

import com.bibisam.dobee.Entity.AuthenticationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisRepository extends CrudRepository<AuthenticationToken, String> {
}
