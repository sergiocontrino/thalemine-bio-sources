package org.intermine.bio.item;

import org.intermine.item.domain.database.ParseException;
import org.intermine.item.domain.database.UnexpectedInputException;

/**
 * Strategy interface for providing the data. <br>
 * 
 * Implementations are expected to be stateful and will be called multiple times
 * for each batch, with each call to {@link #read()} returning a different value
 * and finally returning <code>null</code> when all input data is exhausted.<br>
 * 
 * Implementations need <b>not</b> be thread-safe and clients of a {@link ItemReader}
 * need to be aware that this is the case.<br>
 * 
 * @author Irina Belyaeva
 *  
 */

public interface ItemReader<T> {

	T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException;
	
}
