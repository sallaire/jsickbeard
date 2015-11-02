package org.sallaire.client;

import java.util.concurrent.ExecutionException;

import deluge.api.DelugeFuture;
import deluge.api.response.IntegerResponse;
import deluge.impl.DelugeSession;

public class DelugeClient implements IClient {
	public void addTorrent() throws InterruptedException, ExecutionException {
		DelugeSession session = deluge.impl.DelugeClient.getSession("37.187.19.83");
		DelugeFuture<IntegerResponse> future = session.login("localclient", "12722af2d4dc8867b8f60add573742d219838209:10");
		System.out.println(future.get());
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		new DelugeClient().addTorrent();
	}
}
