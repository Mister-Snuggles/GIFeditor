package mainEditor.observerObject;

/**
 * The enumeration of state changes that can be passed from Observables to Observers. 
 * <p>
 * Copyright 2017-2018 Joey Sun.<p>
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */

public enum ObservedState {
	OPEN, CLOSE, WRITE, SAVE, CHANGED_ADD, CHANGED_UNDO;
}