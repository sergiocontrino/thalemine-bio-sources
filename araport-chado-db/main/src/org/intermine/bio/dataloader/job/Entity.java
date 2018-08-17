package org.intermine.bio.dataloader.job;

import java.io.Serializable;

import org.intermine.bio.dataloader.util.ClassUtils;

@SuppressWarnings("serial")
public class Entity implements Serializable {

	private Long id;

	private volatile Integer version;

	public Entity() {
		super();
	}

	public Entity(Long id) {
		super();

		//Commented out because StepExecutions are still created in a disconnected
		//manner.  The Repository should create them, then this can be uncommented.
		//Assert.notNull(id, "Entity id must not be null.");
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the version
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * Public setter for the version needed only by repository methods.
	 * @param version the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * Increment the version number
	 */
	public void incrementVersion() {
		if (version == null) {
			version = 0;
		} else {
			version = version + 1;
		}
	}

	@Override
	public String toString() {
		return String.format("%s: id=%d, version=%d", ClassUtils.getShortName(getClass()), id, version);
	}

	/**
	 * Attempt to establish identity based on id if both exist. If either id
	 * does not exist use Object.equals().
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if (other == null) {
			return false;
		}
		if (!(other instanceof Entity)) {
			return false;
		}
		Entity entity = (Entity) other;
		if (id == null || entity.getId() == null) {
			return false;
		}
		return id.equals(entity.getId());
	}

	/**
	 * Use ID if it exists to establish hash code, otherwise fall back to
	 * Object.hashCode(). Based on the same information as equals, so if that
	 * changes, this will. N.B. this follows the contract of Object.hashCode(),
	 * but will cause problems for anyone adding an unsaved {@link Entity} to a
	 * Set because Set.contains() will almost certainly return false for the
	 * {@link Entity} after it is saved. Spring Batch does not store any of its
	 * entities in Sets as a matter of course, so internally this is consistent.
	 * Clients should not be exposed to unsaved entities.
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (id == null) {
			return super.hashCode();
		}
		return 39 + 87 * id.hashCode();
	}
	
}
