package ru.mtt.db.repository;

import org.springframework.stereotype.Repository;
import ru.mtt.db.domain.UsersEntity;

import java.util.List;

@Repository
public interface UsersRepository extends BasicRepository<UsersEntity, Integer> {
    // Testing adding custom methods to Repo
    List<UsersEntity> deleteAllByUsername(String username);
}
