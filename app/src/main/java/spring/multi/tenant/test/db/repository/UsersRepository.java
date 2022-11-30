package spring.multi.tenant.test.db.repository;

import org.springframework.stereotype.Repository;
import spring.multi.tenant.test.db.domain.UsersEntity;

import java.util.List;

@Repository
public interface UsersRepository extends BasicRepository<UsersEntity, Integer> {
    // Testing adding custom methods to Repo
    List<UsersEntity> deleteAllByLastName(String lastName);
}
