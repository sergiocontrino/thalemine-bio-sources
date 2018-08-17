package org.intermine.bio.item;

public interface ItemProcessor<I, O> {

	/**
	 * Process the provided item, returning a potentially modified or new item for continued
	 * processing.  If the returned result is null, it is assumed that processing of the item
	 * should not continue.
	 * 
	 * @param item to be processed
	 * @return potentially modified or new item for continued processing, null if processing of the 
	 *  provided item should not continue.
	 * @throws Exception
	 */
	O process(I item) throws Exception;
}
