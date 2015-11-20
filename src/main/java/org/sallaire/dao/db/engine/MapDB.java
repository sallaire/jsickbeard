package org.sallaire.dao.db.engine;

import java.util.ArrayList;
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

	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> getValues(String collection) {
		try (DB db = txMaker.makeTx()) {
			Collection<?> value = db.hashMap(collection).values();
			return new ArrayList<>((Collection<T>) value);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K, V> Map<K, V> getAll(String collection) {
		try (DB db = txMaker.makeTx()) {
			return new HashMap<K, V>((Map<K, V>) db.hashMap(collection));
		}
	}

	@Override
	public <T> void store(String collection, Long id, T value) {
		store(collection, id, value, Serializer.LONG);
	}

	@Override
	public <T> void store(String collection, String id, T value) {
		store(collection, id, value, Serializer.STRING);
	}

	@Override
	public <T> void store(String collection, Object id, T value) {
		store(collection, id, value, Serializer.JAVA);
	}

	public <K, V> void store(String collection, K id, V value, Serializer<K> keySerializer) {
		try (DB db = txMaker.makeTx()) {
			db.hashMap(collection, keySerializer, Serializer.JAVA).put(id, value);
			db.commit();
		}
	}

	@Override
	public <T> T get(String collection, Long id) {
		return get(collection, id, Serializer.LONG);
	}

	@Override
	public <T> T get(String collection, String id) {
		return get(collection, id, Serializer.STRING);
	}

	@Override
	public <T> T get(String collection, Object id) {
		return get(collection, id, Serializer.JAVA);
	}

	@SuppressWarnings("unchecked")
	private <T> T get(String collection, Object id, Serializer<?> keySerializer) {
		try (DB db = txMaker.makeTx()) {
			Object value = db.hashMap(collection, keySerializer, Serializer.JAVA).get(id);
			return (T) value;
		}
	}

	@Override
	public void remove(String collection, Long id) {
		remove(collection, id, Serializer.LONG);
	}

	@Override
	public void remove(String collection, Object id) {
		remove(collection, id, Serializer.JAVA);
	}

	@Override
	public void remove(String collection, String id) {
		remove(collection, id, Serializer.STRING);
	}

	private void remove(String collection, Object id, Serializer<?> keySerializer) {
		try (DB db = txMaker.makeTx()) {
			db.hashMap(collection, keySerializer, Serializer.JAVA).remove(id);
			db.commit();
		}
	}
}
