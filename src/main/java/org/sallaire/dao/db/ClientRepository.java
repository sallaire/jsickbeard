package org.sallaire.dao.db;

import org.sallaire.dao.db.entity.ClientConfiguration;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<ClientConfiguration, Long> {

}
