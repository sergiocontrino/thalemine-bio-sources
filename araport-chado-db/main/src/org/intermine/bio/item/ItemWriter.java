package org.intermine.bio.item;

import java.util.List;

/**
 * <p>
 * Basic interface for generic output operations. Class implementing this
 * interface will be responsible for serializing objects as necessary.
 * Generally, it is responsibility of implementing class to decide which
 * technology to use for mapping and how it should be configured.
 * </p>
 *  
 * 
 * @author Irina Belyaeva
 * 
 */
public interface ItemWriter<T>{

	
	/**
	 * Process the supplied data element. Will not be called with any null items
	 * in normal operation.
	 *
	 * @param items items to be written
	 * @throws Exception if there are errors. The framework will catch the
	 * exception and convert or rethrow it as appropriate.
	 */
	void write(List<? extends T> items) throws Exception;
	
}
