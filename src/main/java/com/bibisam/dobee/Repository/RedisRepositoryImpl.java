package com.bibisam.dobee.Repository;

import com.bibisam.dobee.Entity.AuthenticationToken;

import java.util.Optional;

public class RedisRepositoryImpl implements RedisRepository{

    @Override
    public <S extends AuthenticationToken> S save(S entity) {
        return null;
    }

    @Override
    public <S extends AuthenticationToken> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<AuthenticationToken> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public Iterable<AuthenticationToken> findAll() {
        return null;
    }

    @Override
    public Iterable<AuthenticationToken> findAllById(Iterable<Integer> integers) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public void delete(AuthenticationToken entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends AuthenticationToken> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
