package org.sallaire.dao.db;

import org.sallaire.dao.db.entity.ProviderConfiguration;
import org.springframework.data.repository.CrudRepository;

public interface ProviderRepository extends CrudRepository<ProviderConfiguration, Long> {
	ProviderConfiguration findByName(String name);
}
