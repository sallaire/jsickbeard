package org.sallaire.dao.db;

import org.sallaire.dao.db.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
	User findUserByName(String name);

}
