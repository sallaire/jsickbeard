package org.sallaire.service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.sallaire.dto.admin.JsonResult;
import org.sallaire.dto.admin.JsonResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AdminService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminService.class);

	@Value("${upload.stats.path}")
	private String resultPath;

	public JsonResults getUploadStats() {
		try {
			JsonResults results = new ObjectMapper().readValue(new File(resultPath), JsonResults.class);
			Collection<String> lastKeys = results.getResults().get(results.getResults().size() - 1).getUploadedById().keySet();
			for (JsonResult result : results.getResults()) {
				result.getUploadedById().keySet().retainAll(lastKeys);
			}
			return results;
		} catch (IOException e) {
			LOGGER.error("Unable to load upload stats", e);
		}
		return null;
	}

}
