package org.intermine.bio.dataloader.job;

import java.io.Serializable;
import java.util.Date;

/**
 * Domain representation of a parameter to a batch job. Only the following types
 * can be parameters: String, Long, Date, and Double.  The identifying flag is
 * used to indicate if the parameter is to be used as part of the identification of
 * a job instance.
 *
 * 
 */
@SuppressWarnings("serial")
public class JobParameter implements Serializable {

	private final Object parameter;

	private final ParameterType parameterType;

	private final boolean identifying;

	/**
	 * Construct a new JobParameter as a String.
	 */
	public JobParameter(String parameter, boolean identifying) {
		this.parameter = parameter;
		parameterType = ParameterType.STRING;
		this.identifying = identifying;
	}

	/**
	 * Construct a new JobParameter as a Long.
	 *
	 * @param parameter
	 */
	public JobParameter(Long parameter, boolean identifying) {
		this.parameter = parameter;
		parameterType = ParameterType.LONG;
		this.identifying = identifying;
	}

	/**
	 * Construct a new JobParameter as a Date.
	 *
	 * @param parameter
	 */
	public JobParameter(Date parameter, boolean identifying) {
		this.parameter = parameter;
		parameterType = ParameterType.DATE;
		this.identifying = identifying;
	}

	/**
	 * Construct a new JobParameter as a Double.
	 *
	 * @param parameter
	 */
	public JobParameter(Double parameter, boolean identifying) {
		this.parameter = parameter;
		parameterType = ParameterType.DOUBLE;
		this.identifying = identifying;
	}


	/**
	 * Construct a new JobParameter as a String.
	 */
	public JobParameter(String parameter) {
		this.parameter = parameter;
		parameterType = ParameterType.STRING;
		this.identifying = true;
	}

	/**
	 * Construct a new JobParameter as a Long.
	 *
	 * @param parameter
	 */
	public JobParameter(Long parameter) {
		this.parameter = parameter;
		parameterType = ParameterType.LONG;
		this.identifying = true;
	}

	/**
	 * Construct a new JobParameter as a Date.
	 *
	 * @param parameter
	 */
	public JobParameter(Date parameter) {
		this.parameter = parameter;
		parameterType = ParameterType.DATE;
		this.identifying = true;
	}

	/**
	 * Construct a new JobParameter as a Double.
	 *
	 * @param parameter
	 */
	public JobParameter(Double parameter) {
		this.parameter = parameter;
		parameterType = ParameterType.DOUBLE;
		this.identifying = true;
	}

	public boolean isIdentifying() {
		return identifying;
	}

	/**
	 * @return the value contained within this JobParameter.
	 */
	public Object getValue() {

		if (parameter != null && parameter.getClass().isInstance(Date.class)) {
			return new Date(((Date) parameter).getTime());
		}
		else {
			return parameter;
		}
	}

	/**
	 * @return a ParameterType representing the type of this parameter.
	 */
	public ParameterType getType() {
		return parameterType;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof JobParameter == false) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		JobParameter rhs = (JobParameter) obj;
		return parameter==null ? rhs.parameter==null && parameterType==rhs.parameterType: parameter.equals(rhs.parameter);
	}

	@Override
	public String toString() {
		return parameter == null ? null : (parameterType == ParameterType.DATE ? "" + ((Date) parameter).getTime()
				: parameter.toString());
	}

	@Override
	public int hashCode() {
		return 7 + 21 * (parameter == null ? parameterType.hashCode() : parameter.hashCode());
	}

	/**
	 * Enumeration representing the type of a JobParameter.
	 */
	public enum ParameterType {

		STRING, DATE, LONG, DOUBLE;
	}
}
