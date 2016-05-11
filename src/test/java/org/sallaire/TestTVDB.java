package org.sallaire;

import java.time.Instant;

public class TestTVDB {
	public static void main(String[] args) throws Exception {
		// ShowData data = new TVDBDao().getShowInformation(263677L, "fr");
		// System.out.println(data);
		System.out.println(Instant.now().getEpochSecond());
	}
}
