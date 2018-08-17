package org.intermine.item.domain.database;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.intermine.bio.dataloader.util.JdbcUtils;
import org.intermine.bio.jdbc.core.PreparedStatementSetter;
import org.intermine.bio.jdbc.core.RowMapper;

public class DatabaseItemReader<T> extends AbstractCursorItemReader<T> {

	private static final Logger log = Logger.getLogger(DatabaseItemReader.class);

	public DatabaseItemReader() {
		super();
	}

	RowMapper<T> rowMapper;
	PreparedStatement preparedStatement;
	private PreparedStatementSetter preparedStatementSetter;

	String sql;

	private Map<Integer, Object> parameterValues = new HashMap<Integer, Object>();

	private Map<Integer, Object> sqlParam = new HashMap<Integer, Object>();

	@Override
	protected void openCursor(Connection con) throws SQLException {

		try {

			log.info("Opening Cursor");
			preparedStatement = con
					.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

			applyStatementSettings(preparedStatement);

			setParameters(preparedStatement);

			this.rs = preparedStatement.executeQuery();
			handleWarnings(preparedStatement);

		} catch (SQLException se) {
			throw new SQLException("Executing query generates error:", getSql(), se);
		}

	}

	@Override
	protected T readCursor(ResultSet rs, int currentRow) throws SQLException {
		return rowMapper.mapRow(rs, currentRow);
	}

	private void setParameters(PreparedStatement prepStmt) throws SQLException {

		if (this.parameterValues != null && this.parameterValues.size() > 0) {

			log.info("Setting query parameters.");
			for (Integer key : this.parameterValues.keySet()) {

				log.info("Setting query parameters: " + "index: " + key + ";" + "value: " + parameterValues.get(key));
				prepStmt.setObject(key, parameterValues.get(key));
			}
		} else {
			log.info("No Query Parameters have been set.");
		}

	}

	/**
	 * Set the RowMapper to be used for all calls to read().
	 *
	 * @param rowMapper
	 */
	public void setRowMapper(RowMapper<T> rowMapper) {
		this.rowMapper = rowMapper;
	}

	@Override
	public String getSql() {

		return this.sql;
	}

	/**
	 * Set the SQL statement to be used when creating the cursor. This statement
	 * should be a complete and valid SQL statement, as it will be run directly
	 * without any modification.
	 *
	 * @param sql
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}

	/**
	 * Set the PreparedStatementSetter to use if any parameter values that need
	 * to be set in the supplied query.
	 *
	 * @param preparedStatementSetter
	 */
	public void setPreparedStatementSetter(PreparedStatementSetter preparedStatementSetter) {
		this.preparedStatementSetter = preparedStatementSetter;
	}

	/**
	 * The parameter values to be used for the query execution. If you use named
	 * parameters then the key should be the name used in the query clause. If
	 * you use "?" placeholders then the key should be the relative index that
	 * the parameter appears in the query string built using the select, from
	 * and where clauses specified.
	 * 
	 * @param parameterValues
	 *            the values keyed by the parameter named/index used in the
	 *            query string.
	 */
	public void setParameterValues(Map<Integer, Object> parameterValues) {
		this.parameterValues = parameterValues;
	}

	public Map<Integer, Object> getParameterMap() {
		Map<Integer, Object> parameterMap = new LinkedHashMap<Integer, Object>();

		if (this.parameterValues != null) {
			parameterMap.putAll(this.parameterValues);
		}

		log.debug("Using parameterMap:" + parameterMap);
		
		return parameterMap;
	}

	/**
	 * Close the cursor and database connection.
	 */
	@Override
	protected void cleanupOnClose() throws Exception {
		JdbcUtils.closeStatement(this.preparedStatement);
	}

	public void open() throws Exception {
		super.doOpen();
	}

	T getInstance() {
		ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
		Class<T> type = (Class<T>) superClass.getActualTypeArguments()[0];
		try {
			return type.newInstance();
		} catch (Exception e) {
			// Oops, no default constructor
			throw new RuntimeException(e);
		}
	}

}
