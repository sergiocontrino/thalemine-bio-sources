package org.intermine.bio.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public interface PreparedStatementSetter {

	/**
	 * Set parameter values on the given PreparedStatement.
	 * @param ps the PreparedStatement to invoke setter methods on
	 * @throws SQLException if a SQLException is encountered
	 * (i.e. there is no need to catch SQLException)
	 */
	void setValues(PreparedStatement ps) throws SQLException;

	void setValues(Map<Integer, Object> param, PreparedStatement ps) throws SQLException;

}