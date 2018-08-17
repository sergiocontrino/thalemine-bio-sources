package org.intermine.bio.dataloader.job;

/**
 * Strategy interface for the generation of the key used in identifying
 * unique {@link JobInstance}.
 *
 * 
 * @param <T> The type of the source data used to calculate the key.
 * @since 2.2
 */
public interface JobKeyGenerator<T> {

	/**
	 * Method to generate the unique key used to identify a job instance.
	 *
	 * @param source Source information used to generate the key
	 *
	 * @return a unique string identifying the job based on the information
	 * supplied
	 */
	String generateKey(T source);
}
