package org.intermine.bio.datalineage;

public class Counter {

	int counter;

    /**
     * Construct a counter whose value is zero.
     */
    public Counter()
    {
	counter = 0;
    }

    /**
     * Construct a counter with given initial value.
     * @param init is the initial value of the counter
     */

    public Counter(int init)
    {
	counter = init;
    }

    /**
     * Returns the value of the counter.
     * @return the value of the counter
     */
    public int getValue()
    {
	return counter;
    }

    /**
     * Sets the value of the counter.
     */
    public void setValue(int init)
    {
    	counter = init;
    }
    
    /**
     * Zeros the counter so getValue() == 0.
     */
    public void clear()
    {
	counter = 0;
    }

    /**
     * Increase the value of the counter by one.
     */
    public void increment()
    {
	counter++;
    }

    /**
     * Return a string representing the value of this counter.
     * @return a string representation of the value
     */
    
    public String toString()
    {
	return ""+counter;
    }

	
}
