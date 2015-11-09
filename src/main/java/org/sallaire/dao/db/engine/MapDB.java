package org.sallaire.dao.db.engine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> getValues(String collection) {
		try (DB db = txMaker.makeTx()) {
			Collection<?> value = db.hashMap(collection, Serializer.LONG, Serializer.JAVA).values();
			return (Collection<T>) value;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Map<Long, T> getAll(String collection) {
		try (DB db = txMaker.makeTx()) {
			return new HashMap<Long, T>((Map<Long, T>) db.hashMap(collection, Serializer.LONG, Serializer.JAVA));
		}
	}

	@Override
	public <T> void store(String collection, String id, T value) {
		try (DB db = txMaker.makeTx()) {
			db.hashMap(collection, Serializer.STRING, Serializer.JAVA).put(id, value);
			db.commit();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String collection, String id) {
		try (DB db = txMaker.makeTx()) {
			Object value = db.hashMap(collection, Serializer.STRING, Serializer.JAVA).get(id);
			return (T) value;
		}
	}

	@Override
	public void remove(String collection, Long id) {
		try (DB db = txMaker.makeTx()) {
			db.hashMap(collection, Serializer.STRING, Serializer.JAVA).remove(id);
			db.commit();
		}
	}
}
