package org.sallaire.dao.db;

import org.sallaire.dao.db.entity.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface UserRepository extends GraphRepository<User> {
	@Query("MATCH (user:User {name:{0}}) RETURN user")
	User getUserFromName(String name);

}
