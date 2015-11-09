package org.sallaire.dao.db;

import org.sallaire.dao.db.engine.IDBEngine;
import org.sallaire.dto.ClientConfiguration;
import org.sallaire.dto.ProviderConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ConfigurationDao {

	@Autowired
	private IDBEngine dbEngine;

	public void saveProvider(String provider, ProviderConfiguration configuration) {
		dbEngine.store(IDBEngine.PROVIDER_CONFIGURATION, provider, configuration);
	}

	public ProviderConfiguration getProvider(String provider) {
		return dbEngine.get(IDBEngine.PROVIDER_CONFIGURATION, provider);
	}

	public void saveClientConfiguration(ClientConfiguration configuration) {
		dbEngine.store(IDBEngine.CLIENT_CONFIGURATION, 0L, configuration);
	}

	public ClientConfiguration getClientConfiguration() {
		return dbEngine.get(IDBEngine.CLIENT_CONFIGURATION, 0L);
	}
}
