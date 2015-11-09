package org.sallaire.provider.t411;

import java.util.ArrayList;
import java.util.List;

public enum Option {
	NUMBER(0), LANG(1), QUALITY(2);

	private Option(int value) {
		this.value = 1 << value;
	}

	private int value;

	public int getValue() {
		return value;
	}

	/**
	 * Identify if the given option is activated or not.
	 * 
	 * @param options
	 *            current options value
	 * @param option
	 *            asked option
	 * @return boolean true if the option is activated
	 */
	public boolean isActivated(int options) {
		return (options & this.getValue()) != 0;
	}

	public static List<Integer> getOptionsToCheck() {
		Integer allOptions = 0;
		for (Option option : values()) {
			allOptions |= option.getValue();
		}

		List<Integer> optionsToCheck = new ArrayList<>();
		optionsToCheck.add(allOptions);
		for (int i = 0; i < values().length; i++) {
			optionsToCheck.add(allOptions & ~i);
		}
		for (int i = 0; i < values().length; i++) {
			optionsToCheck.add(0 | i);
		}
		optionsToCheck.add(0);

		return optionsToCheck;
	}
}