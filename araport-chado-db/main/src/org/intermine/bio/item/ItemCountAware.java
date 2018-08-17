package org.intermine.bio.item;
/**
 * Marker interface indicating that an item should have the item count set on it. Typically used within
 * an {@link AbstractItemCountingItemStreamItemReader}.
 *
 * @author Jimmy Praet
 */
public interface ItemCountAware {

	/**
	 * Setter for the injection of the current item count.
	 * 
	 * @param count the number of items that have been processed in this execution.
	 */
	void setItemCount(int count);
}
