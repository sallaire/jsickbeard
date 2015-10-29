package org.sallaire.dao.db.engine;

import javax.annotation.PostConstruct;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
import org.mapdb.TxMaker;
import org.springframework.stereotype.Repository;

@Repository
public class MapDB implements IDBEngine {

	private TxMaker txMaker;

	@PostConstruct
	public void init() {
		txMaker = DBMaker.fileDB(DB_LOCATION.toFile()).closeOnJvmShutdown().fileMmapEnableIfSupported().makeTxMaker();
	}

	@Override
	public <T> void store(String collection, Long id, T value) {
		try (DB db = txMaker.makeTx()) {
			db.hashMap(collection, Serializer.LONG, Serializer.JAVA).put(id, value);
			db.commit();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String collection, Long id) {
		try (DB db = txMaker.makeTx()) {
			Object value = db.hashMap(collection, Serializer.LONG, Serializer.JAVA).get(id);
			return (T) value;
		}
	}
}
