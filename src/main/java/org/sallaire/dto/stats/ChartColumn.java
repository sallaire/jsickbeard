package org.sallaire.dto.stats;

public class ChartColumn {
	private String label;
	private String type;

	public ChartColumn() {
		super();
	}

	public ChartColumn(String label, String type) {
		super();
		this.label = label;
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
