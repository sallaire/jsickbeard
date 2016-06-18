package org.sallaire.dto.stats;

import java.util.ArrayList;
import java.util.List;

public class ChartData {
	private List<ChartColumn> cols;
	private List<ChartRow> rows;

	public ChartData() {
		super();
		cols = new ArrayList<>();
		rows = new ArrayList<>();
	}

	public List<ChartColumn> getCols() {
		return cols;
	}

	public void setCols(List<ChartColumn> cols) {
		this.cols = cols;
	}

	public List<ChartRow> getRows() {
		return rows;
	}

	public void setRows(List<ChartRow> rows) {
		this.rows = rows;
	}
}
