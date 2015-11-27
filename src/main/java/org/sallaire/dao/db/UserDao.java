package org.sallaire.dao.db;

import java.util.Collection;
import java.util.Optional;

import org.sallaire.dao.db.engine.IDBEngine;
import org.sallaire.dto.user.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
	@Autowired
	private IDBEngine dbEngine;

	public void saveAccount(Account account) {
		dbEngine.store(IDBEngine.ACCOUNT, account.getUser(), account);
	}

	public Optional<Account> getAccount(String name) {
		return Optional.ofNullable(dbEngine.get(IDBEngine.ACCOUNT, name));
	}

	public Optional<Collection<Account>> getAccounts() {
		return Optional.ofNullable(dbEngine.getValues(IDBEngine.ACCOUNT));
	}

	public void deleteUser(String name) {
		dbEngine.remove(IDBEngine.ACCOUNT, name);
	}

}
