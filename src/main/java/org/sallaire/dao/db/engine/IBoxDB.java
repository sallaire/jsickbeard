package org.sallaire.dao.db.engine;

import iBoxDB.LocalServer.DB;

public class IBoxDB implements IDBEngine {

	public void init(String path) {
		DB.root(path);
	}

	@Override
	public <T> void store(String collection, Long id, T value) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> T get(String collection, Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
