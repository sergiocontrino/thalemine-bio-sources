package org.intermine.item.domain.database;

/*
 * Copyright (C) 2002-2015 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.intermine.bio.dataloader.util.JdbcUtils;
import org.intermine.bio.item.ReaderNotOpenException;

public abstract class AbstractCursorItemReader<T>
    extends AbstractItemCountingItemStreamItemReader<T>
{
    private static final Logger LOG = Logger.getLogger(AbstractCursorItemReader.class);
    public static final int VALUE_NOT_SET = -1;
    private int fetchSize = VALUE_NOT_SET;
    private int maxRows = VALUE_NOT_SET;
    private int queryTimeout = VALUE_NOT_SET;
    private int currentItemCount;
    private int itemCount;
    private Connection con;
    protected ResultSet rs;
    private boolean ignoreWarnings = true;
    private boolean verifyCursorPosition = true;
    private boolean initialized = false;

    public boolean isInitialized() {
        return initialized;
    }

    private boolean driverSupportsAbsolute = false;
    private boolean useSharedExtendedConnection = false;

    public AbstractCursorItemReader() {
        super();
    }

    protected void initializeConnection() throws Exception {
        if (getDataSource() == null) {
            throw new Exception("Connection must not be null.");
        }
    }

    public Connection getDataSource() {
        return this.con;
    }


    /**
     * Public setter for the data source for injection purposes.
     *
     */
    public void setDataSource(Connection connection) {
        this.con = connection;
    }

    /**
     * Prepare the given JDBC Statement (or PreparedStatement or
     * CallableStatement), applying statement settings such as fetch size, max
     * rows, and query timeout. @param stmt the JDBC Statement to prepare
     *
     * @param stmt {@link java.sql.PreparedStatement} to be configured
     *
     * @throws SQLException if interactions with provided stmt fail
     *
     * @see #setFetchSize
     * @see #setMaxRows
     * @see #setQueryTimeout
     */
    protected void applyStatementSettings(PreparedStatement stmt) throws SQLException {
        if (fetchSize != VALUE_NOT_SET) {
            stmt.setFetchSize(fetchSize);
            stmt.setFetchDirection(ResultSet.FETCH_FORWARD);
        }
        if (maxRows != VALUE_NOT_SET) {
            stmt.setMaxRows(maxRows);
        }
        if (queryTimeout != VALUE_NOT_SET) {
            stmt.setQueryTimeout(queryTimeout);
        }
    }

    /**
     * Throw a SQLWarningException if we're not ignoring warnings, else log the
     * warnings (at debug level).
     *
     * @param statement the current statement to obtain the warnings from, if there are any.
     * @throws SQLException if interaction with provided statement fails.
     *
     *
     */
    protected void handleWarnings(Statement statement) throws SQLWarning,
    SQLException {
        if (ignoreWarnings) {
            if (LOG.isDebugEnabled()) {
                SQLWarning warningToLog = statement.getWarnings();
                while (warningToLog != null) {
                    LOG.debug("SQLWarning ignored: SQL state '" + warningToLog.getSQLState()
                            + "', error code '" + warningToLog.getErrorCode()
                            + "', message [" + warningToLog.getMessage() + "]");
                    warningToLog = warningToLog.getNextWarning();
                }
            }
        }
        else {
            SQLWarning warnings = statement.getWarnings();
            if (warnings != null) {
                throw new SQLWarning("Warning not ignored", warnings);
            }
        }
    }

    /**
     * Gives the JDBC driver a hint as to the number of rows that should be
     * fetched from the database when more rows are needed for this
     * <code>ResultSet</code> object. If the fetch size specified is zero, the
     * JDBC driver ignores the value.
     *
     * @param fetchSize the number of rows to fetch
     * @see ResultSet#setFetchSize(int)
     */
    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    /**
     * Sets the limit for the maximum number of rows that any
     * <code>ResultSet</code> object can contain to the given number.
     *
     * @param maxRows the new max rows limit; zero means there is no limit
     * @see Statement#setMaxRows(int)
     */
    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    /**
     * Sets the number of seconds the driver will wait for a
     * <code>Statement</code> object to execute to the given number of seconds.
     * If the limit is exceeded, an <code>SQLException</code> is thrown.
     *
     * @param queryTimeout seconds the new query timeout limit in seconds; zero
     * means there is no limit
     * @see Statement#setQueryTimeout(int)
     */
    public void setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

    /**
     * Set whether SQLWarnings should be ignored (only logged) or exception
     * should be thrown.
     *
     * @param ignoreWarnings if TRUE, warnings are ignored
     */
    public void setIgnoreWarnings(boolean ignoreWarnings) {
        this.ignoreWarnings = ignoreWarnings;
    }

    /**
     * Allow verification of cursor position after current row is processed by
     * RowMapper or RowCallbackHandler. Default value is TRUE.
     *
     * @param verifyCursorPosition if true, cursor position is verified
     */

    public abstract String getSql();

    protected abstract void openCursor(Connection con) throws SQLException;

    protected abstract T readCursor(ResultSet rs, int currentRow) throws SQLException;

    /**
     * Execute the statement to open the cursor.
     */
    protected void doOpen() throws Exception {

        initializeConnection();
        openCursor(con);
        initialized = true;

    }

    /**227
     * Read next row and map it to item
     *
     */
    protected T doRead() throws Exception {
        if (rs == null) {
            LOG.error("ResultSet is NULL");
            throw new ReaderNotOpenException("Reader must be open before it can be read.");
        }

        try {
            LOG.debug("Reading Item");
            LOG.debug("Current Row Count 1:" + currentItemCount);
            if (!rs.next()) {
                LOG.debug("No Records in ResultSet!");
                return null;
                } else {
                LOG.debug("Found Records in ResultSet!");
            }
            LOG.debug("Reading Item");
            currentItemCount++;
            setItemCount(currentItemCount);

            LOG.debug("Current Row Count 2:" + currentItemCount);
            int currentRow = getCurrentItemCount();
            T item = readCursor(rs, currentRow);
            return item;
        } catch (SQLException se) {
            LOG.error("Attempt to process next row failed: SQL: \n"
                    + getSql() + "; SQL Exception:" + se.getMessage());
            se.printStackTrace();
            throw new SQLException("Attempt to process next row failed", getSql(), se);
        }
    }

    protected int getCurrentItemCount() {
        return currentItemCount;
    }

    public void setCurrentItemCount(int count) {
        this.currentItemCount = count;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    /**
     * Close the cursor and database connection.
     * Make call to cleanupOnClose so sub classes can cleanup
     * any resources they have allocated.
     */
    @Override
    protected void doClose() throws Exception {
        initialized = false;
        JdbcUtils.closeResultSet(this.rs);
        rs = null;
        cleanupOnClose();
    }

    protected abstract void cleanupOnClose()  throws Exception;

    public ResultSet getResultSet() {
        return rs;
    }

    public boolean next() throws SQLException {
        return this.rs.next();
    }

    public boolean hasNext() throws SQLException {
        return !rs.isLast();
    }
}
