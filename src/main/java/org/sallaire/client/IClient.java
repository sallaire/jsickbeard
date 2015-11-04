package org.sallaire.client;

import java.io.IOException;
import java.nio.file.Path;

public interface IClient {
	void addTorrent(Path torrentPath, String torrentName) throws IOException;
}
