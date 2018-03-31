package mainEditor.history;

/**
 * Interface that details what a History object should do/keep track of.
 * Stored in memory as a linked list; it keeps track of exactly one earlier and later 
 * historical record.
 *<p>
 * Copyright 2017-2018 Joey Sun.<p>
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
public interface History {
	
	/**
	 *  Returns if there is a next record in the history.
	 *  @return If a next record exists.
	 */
	public boolean hasNext();	
	
	/**
	 * Returns next record in history.
	 * @return Next record in history.
	 */
	public History next();

	/**
	 * Returns if there is a previous record in the history.
	 * @return If a previous record exists.
	 */
	public boolean hasPrev();
	
	/**
	 * Returns previous record in history.
	 * @return Previous record in history.
	 */
	public History prev();
	
	/**
	 * Returns true if this historical record is the earliest of its kind.
	 * @return True if it is the earliest historical record; false otherwise.
	 */
	public boolean isFirst();
	
	/**
	 * Returns true if this historical record is the most recent of its kind.
	 * @return True if it is the most recent historical record; false otherwise.
	 */
	public boolean isLast();
	
	/**
	 * Sets which historical record the current historical record will point to next.
	 * @param h More recent historical record to point to.
	 */
	public void setNext(History h);
	
	/**
	 * Sets which historical record the current historical record will point to previously.
	 * @param h Earlier historical record to point to.
	 */
	public void setPrev(History h);
	
	/**
	 * Removes current historical record, and gives a pointer to the earlier history that 
	 * is connected to it. Returns null if this is last historical record.
	 * @return The earlier historical record.
	 */
	public History undo();
}
