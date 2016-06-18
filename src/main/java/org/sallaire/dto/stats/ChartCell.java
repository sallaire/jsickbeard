package org.sallaire.dto.stats;

public class ChartCell {
	private Object v;

	public ChartCell(Object v) {
		super();
		this.v = String.valueOf(v);
	}

	public ChartCell(String v) {
		super();
		this.v = v;
	}

	public ChartCell() {
		super();
	}

	public Object getV() {
		return v;
	}

	public void setV(Object v) {
		this.v = v;
	}
}
