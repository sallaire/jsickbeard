package info.movito.themoviedbapi.model.changes;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

// todo fix or remove this
public class ChangesItems {

	@JsonProperty("changes")
	private List<ChangeKeyItem> changedItems = new ArrayList<ChangeKeyItem>();

	public List<ChangeKeyItem> getChangedItems() {
		return changedItems;
	}

	public void setChangedItems(List<ChangeKeyItem> changes) {
		this.changedItems = changes;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
