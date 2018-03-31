package mainEditor.imageAction;

/**
 * A list of options that are used when confirming whether or not to undo the history of the animation
 * by one step.
 * <p>
 * Copyright 2017-2018 Joey Sun.<p>
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */

public enum UndoHistoryOptions {
	YES_OPTION("Yes"), NO_OPTION("Go Back"), CANCEL_OPTION("Please go away."), EASTER_EGG("WHAT?!");
	
	/**
	 * Defines messages for each option.
	 * @param text Message of command/option.
	 */
	private UndoHistoryOptions(String text) {
		this.text = text;
	}

	private String text;	// message of option

	/**
	 * Returns message of option.
	 * @return Message of option.
	 */
	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return getText();
	}
}
