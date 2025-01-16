package com.bibisam.dobee.Repository;

import com.bibisam.dobee.Entity.AuthenticationToken;
import org.springframework.data.repository.CrudRepository;


public interface RedisRepository extends CrudRepository<AuthenticationToken, Integer> {
}
