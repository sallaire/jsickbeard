package org.sallaire.dto.stats;

import java.util.ArrayList;
import java.util.List;

public class ChartRow {
	List<ChartCell> c;

	public ChartRow() {
		super();
		c = new ArrayList<>();
	}

	public List<ChartCell> getC() {
		return c;
	}

	public void setC(List<ChartCell> c) {
		this.c = c;
	}

}
