package org.sallaire.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.sallaire.dto.stats.ChartCell;
import org.sallaire.dto.stats.ChartColumn;
import org.sallaire.dto.stats.ChartData;
import org.sallaire.dto.stats.ChartRow;
import org.sallaire.dto.stats.JsonResult;
import org.sallaire.dto.stats.JsonResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class StatsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(StatsService.class);

	@Value("${upload.stats.path}")
	private String resultPath;

	public ChartData getUploadStats(Long filter) {
		ChartData chartData = new ChartData();
		try {
			JsonResults results = new ObjectMapper().readValue(new File(resultPath), JsonResults.class);
			Collection<String> filteredKeys = new ArrayList<>(results.getResults().get(results.getResults().size() - 1).getUploadedById().keySet());
			Map<String, Long> firstValues = new HashMap<>();

			if (filter != null) {
				for (JsonResult jsonResult : results.getResults()) {
					for (Entry<String, Long> entry : jsonResult.getUploadedById().entrySet()) {
						if (firstValues.containsKey(entry.getKey())) {
							Long firstValue = firstValues.get(entry.getKey());
							double upload = ((double) (entry.getValue() - firstValue)) / 1024 / 1024;
							if (upload > filter) {
								filteredKeys.remove(entry.getKey());
							}
						} else {
							firstValues.put(entry.getKey(), entry.getValue());
						}
					}
				}
			}

			firstValues = new HashMap<>();
			for (JsonResult jsonResult : results.getResults()) {
				ChartRow row = new ChartRow();

				row.getC().add(new ChartCell("Date(" + jsonResult.getTime() + ")"));
				for (String key : filteredKeys) {
					if (jsonResult.getUploadedById().containsKey(key)) {
						Long value = jsonResult.getUploadedById().get(key);
						if (firstValues.containsKey(key)) {
							Long firstValue = firstValues.get(key);
							double upload = ((double) (value - firstValue)) / 1024 / 1024;
							row.getC().add(new ChartCell(upload));
						} else {
							firstValues.put(key, value);
							row.getC().add(new ChartCell("0"));
						}
					} else {
						row.getC().add(new ChartCell("0"));
					}
				}
				chartData.getRows().add(row);
			}

			chartData.getCols().add(new ChartColumn("X", "date"));
			for (Entry<String, String> entry : results.getNameById().entrySet()) {
				if (filteredKeys.contains(entry.getKey())) {
					chartData.getCols().add(new ChartColumn(entry.getValue(), "number"));
				}
			}
		} catch (IOException e) {
			LOGGER.error("Unable to load upload stats", e);
		}
		return chartData;
	}

}
