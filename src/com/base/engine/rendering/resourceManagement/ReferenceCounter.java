package com.base.engine.rendering.resourceManagement;

/**
 * 
 * Manage a count of all open references to a resource.
 * The count defaults to 0 so you must call AddReference() to 
 * initialize the first reference count.
 *
 */
public abstract class ReferenceCounter {
	
	/**
	 * The number of open references currently held to the resource this object represents
	 */
	private int m_refCount = 0;
	
	/**
	 * Add a reference to the current count
	 */
	public void AddReference()
	{
		m_refCount++;
	}

	/**
	 * Remove a reference from the count
	 * @return true if there are no more references
	 */
	public boolean RemoveReference()
	{
		m_refCount--;
		return m_refCount <= 0; // Handle a less than zero case to make sure that bad code doesn't cause memory leaks
	}
	
}