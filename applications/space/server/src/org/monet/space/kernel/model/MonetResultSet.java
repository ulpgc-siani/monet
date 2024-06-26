package org.monet.space.kernel.model;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

public class MonetResultSet implements ResultSet {
	Connection connection;
	ResultSet resultSet;

	public MonetResultSet(Connection connection, ResultSet resultSet) {
		this.connection = connection;
		this.resultSet = resultSet;
	}

	@Override
	public void close() throws SQLException {
		try {
			this.resultSet.close();
		} finally {
			this.connection.close();
		}
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return this.resultSet.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return this.resultSet.isWrapperFor(iface);
	}

	@Override
	public boolean next() throws SQLException {
		return this.resultSet.next();
	}


	@Override
	public boolean wasNull() throws SQLException {
		return this.resultSet.wasNull();
	}

	@Override
	public String getString(int columnIndex) throws SQLException {
		return this.resultSet.getString(columnIndex);
	}

	@Override
	public boolean getBoolean(int columnIndex) throws SQLException {
		return this.resultSet.getBoolean(columnIndex);
	}

	@Override
	public byte getByte(int columnIndex) throws SQLException {
		return this.resultSet.getByte(columnIndex);
	}

	@Override
	public short getShort(int columnIndex) throws SQLException {
		return this.resultSet.getShort(columnIndex);
	}

	@Override
	public int getInt(int columnIndex) throws SQLException {
		return this.resultSet.getInt(columnIndex);
	}

	@Override
	public long getLong(int columnIndex) throws SQLException {
		return this.resultSet.getLong(columnIndex);
	}

	@Override
	public float getFloat(int columnIndex) throws SQLException {
		return this.resultSet.getFloat(columnIndex);
	}

	@Override
	public double getDouble(int columnIndex) throws SQLException {
		return this.resultSet.getDouble(columnIndex);
	}

	@Deprecated
	public BigDecimal getBigDecimal(int columnIndex, int scale)
		throws SQLException {
		return this.resultSet.getBigDecimal(columnIndex);
	}

	@Override
	public byte[] getBytes(int columnIndex) throws SQLException {
		return this.resultSet.getBytes(columnIndex);
	}

	@Override
	public Date getDate(int columnIndex) throws SQLException {
		return this.resultSet.getDate(columnIndex);
	}

	@Override
	public Time getTime(int columnIndex) throws SQLException {
		return this.resultSet.getTime(columnIndex);
	}

	@Override
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return this.resultSet.getTimestamp(columnIndex, Calendar.getInstance(TimeZone.getTimeZone("UTC")));
	}

	@Override
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		return this.resultSet.getAsciiStream(columnIndex);
	}

	@Deprecated
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		return this.resultSet.getAsciiStream(columnIndex);
	}

	@Override
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		return this.resultSet.getAsciiStream(columnIndex);
	}

	@Override
	public String getString(String columnLabel) throws SQLException {
		return this.resultSet.getString(columnLabel);
	}

	@Override
	public boolean getBoolean(String columnLabel) throws SQLException {
		return this.resultSet.getBoolean(columnLabel);
	}

	@Override
	public byte getByte(String columnLabel) throws SQLException {
		return this.resultSet.getByte(columnLabel);
	}

	@Override
	public short getShort(String columnLabel) throws SQLException {
		return this.resultSet.getShort(columnLabel);
	}

	@Override
	public int getInt(String columnLabel) throws SQLException {
		return this.resultSet.getInt(columnLabel);
	}

	@Override
	public long getLong(String columnLabel) throws SQLException {
		return this.resultSet.getLong(columnLabel);
	}

	@Override
	public float getFloat(String columnLabel) throws SQLException {
		return this.resultSet.getFloat(columnLabel);
	}

	@Override
	public double getDouble(String columnLabel) throws SQLException {
		return this.resultSet.getDouble(columnLabel);
	}

	@Deprecated
	public BigDecimal getBigDecimal(String columnLabel, int scale)
		throws SQLException {
		return this.resultSet.getBigDecimal(columnLabel);
	}

	@Override
	public byte[] getBytes(String columnLabel) throws SQLException {
		return this.resultSet.getBytes(columnLabel);
	}

	@Override
	public Date getDate(String columnLabel) throws SQLException {
		return this.resultSet.getDate(columnLabel);
	}

	@Override
	public Time getTime(String columnLabel) throws SQLException {
		return this.resultSet.getTime(columnLabel);
	}

	@Override
	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		return this.resultSet.getTimestamp(columnLabel, Calendar.getInstance(TimeZone.getTimeZone("UTC")));
	}

	@Override
	public InputStream getAsciiStream(String columnLabel) throws SQLException {
		return this.resultSet.getAsciiStream(columnLabel);
	}

	@Deprecated
	public InputStream getUnicodeStream(String columnLabel) throws SQLException {
		return this.resultSet.getUnicodeStream(columnLabel);
	}

	@Override
	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		return this.resultSet.getBinaryStream(columnLabel);
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return this.resultSet.getWarnings();
	}

	@Override
	public void clearWarnings() throws SQLException {
		this.resultSet.clearWarnings();
	}

	@Override
	public String getCursorName() throws SQLException {
		return this.resultSet.getCursorName();
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return this.resultSet.getMetaData();
	}

	@Override
	public Object getObject(int columnIndex) throws SQLException {
		return this.resultSet.getObject(columnIndex);
	}

	@Override
	public Object getObject(String columnLabel) throws SQLException {
		return this.resultSet.getObject(columnLabel);
	}

	@Override
	public int findColumn(String columnLabel) throws SQLException {
		return this.resultSet.findColumn(columnLabel);
	}

	@Override
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		return this.resultSet.getCharacterStream(columnIndex);
	}

	@Override
	public Reader getCharacterStream(String columnLabel) throws SQLException {
		return this.resultSet.getCharacterStream(columnLabel);
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return this.resultSet.getBigDecimal(columnIndex);
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		return this.resultSet.getBigDecimal(columnLabel);
	}

	@Override
	public boolean isBeforeFirst() throws SQLException {
		return this.resultSet.isBeforeFirst();
	}

	@Override
	public boolean isAfterLast() throws SQLException {
		return this.resultSet.isAfterLast();
	}

	@Override
	public boolean isFirst() throws SQLException {
		return this.resultSet.isFirst();
	}

	@Override
	public boolean isLast() throws SQLException {
		return this.resultSet.isLast();
	}

	@Override
	public void beforeFirst() throws SQLException {
		this.resultSet.beforeFirst();
	}

	@Override
	public void afterLast() throws SQLException {
		this.resultSet.afterLast();
	}

	@Override
	public boolean first() throws SQLException {
		return this.resultSet.first();
	}

	@Override
	public boolean last() throws SQLException {
		return this.resultSet.last();
	}

	@Override
	public int getRow() throws SQLException {
		return this.resultSet.getRow();
	}

	@Override
	public boolean absolute(int row) throws SQLException {
		return this.resultSet.absolute(row);
	}

	@Override
	public boolean relative(int rows) throws SQLException {
		return this.resultSet.relative(rows);
	}

	@Override
	public boolean previous() throws SQLException {
		return this.resultSet.previous();
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		this.resultSet.setFetchDirection(direction);
	}

	@Override
	public int getFetchDirection() throws SQLException {
		return this.resultSet.getFetchDirection();

	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		this.resultSet.setFetchSize(rows);
	}

	@Override
	public int getFetchSize() throws SQLException {
		return this.resultSet.getFetchSize();
	}

	@Override
	public int getType() throws SQLException {
		return this.resultSet.getType();
	}

	@Override
	public int getConcurrency() throws SQLException {
		return this.resultSet.getConcurrency();
	}

	@Override
	public boolean rowUpdated() throws SQLException {
		return this.resultSet.rowUpdated();
	}

	@Override
	public boolean rowInserted() throws SQLException {
		return this.resultSet.rowInserted();
	}

	@Override
	public boolean rowDeleted() throws SQLException {
		return this.resultSet.rowDeleted();
	}

	@Override
	public void updateNull(int columnIndex) throws SQLException {
		this.resultSet.updateNull(columnIndex);
	}

	@Override
	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		this.resultSet.updateBoolean(columnIndex, x);
	}

	@Override
	public void updateByte(int columnIndex, byte x) throws SQLException {
		this.resultSet.updateByte(columnIndex, x);
	}

	@Override
	public void updateShort(int columnIndex, short x) throws SQLException {
		this.resultSet.updateShort(columnIndex, x);
	}

	@Override
	public void updateInt(int columnIndex, int x) throws SQLException {
		this.resultSet.updateInt(columnIndex, x);
	}

	@Override
	public void updateLong(int columnIndex, long x) throws SQLException {
		this.resultSet.updateLong(columnIndex, x);
	}

	@Override
	public void updateFloat(int columnIndex, float x) throws SQLException {
		this.resultSet.updateFloat(columnIndex, x);
	}

	@Override
	public void updateDouble(int columnIndex, double x) throws SQLException {
		this.resultSet.updateDouble(columnIndex, x);
	}

	@Override
	public void updateBigDecimal(int columnIndex, BigDecimal x)
		throws SQLException {
		this.resultSet.updateBigDecimal(columnIndex, x);
	}

	@Override
	public void updateString(int columnIndex, String x) throws SQLException {
		this.resultSet.updateString(columnIndex, x);
	}

	@Override
	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		this.resultSet.updateBytes(columnIndex, x);
	}

	@Override
	public void updateDate(int columnIndex, Date x) throws SQLException {
		this.resultSet.updateDate(columnIndex, x);
	}

	@Override
	public void updateTime(int columnIndex, Time x) throws SQLException {
		this.resultSet.updateTime(columnIndex, x);
	}

	@Override
	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
		this.resultSet.updateTimestamp(columnIndex, x);
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, int length)
		throws SQLException {
		this.resultSet.updateAsciiStream(columnIndex, x);
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, int length)
		throws SQLException {
		this.resultSet.updateBinaryStream(columnIndex, x);
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, int length)
		throws SQLException {
		this.resultSet.updateCharacterStream(columnIndex, x);
	}

	@Override
	public void updateObject(int columnIndex, Object x, int scaleOrLength)
		throws SQLException {
		this.resultSet.updateObject(columnIndex, x, scaleOrLength);
	}

	@Override
	public void updateObject(int columnIndex, Object x) throws SQLException {
		this.resultSet.updateObject(columnIndex, x);
	}

	@Override
	public void updateNull(String columnLabel) throws SQLException {
		this.resultSet.updateNull(columnLabel);
	}

	@Override
	public void updateBoolean(String columnLabel, boolean x) throws SQLException {
		this.resultSet.updateBoolean(columnLabel, x);
	}

	@Override
	public void updateByte(String columnLabel, byte x) throws SQLException {
		this.resultSet.updateByte(columnLabel, x);
	}

	@Override
	public void updateShort(String columnLabel, short x) throws SQLException {
		this.resultSet.updateShort(columnLabel, x);
	}

	@Override
	public void updateInt(String columnLabel, int x) throws SQLException {
		this.resultSet.updateInt(columnLabel, x);
	}

	@Override
	public void updateLong(String columnLabel, long x) throws SQLException {
		this.resultSet.updateLong(columnLabel, x);
	}

	@Override
	public void updateFloat(String columnLabel, float x) throws SQLException {
		this.resultSet.updateFloat(columnLabel, x);
	}

	@Override
	public void updateDouble(String columnLabel, double x) throws SQLException {
		this.resultSet.updateDouble(columnLabel, x);
	}

	@Override
	public void updateBigDecimal(String columnLabel, BigDecimal x)
		throws SQLException {
		this.resultSet.updateBigDecimal(columnLabel, x);
	}

	@Override
	public void updateString(String columnLabel, String x) throws SQLException {
		this.resultSet.updateString(columnLabel, x);
	}

	@Override
	public void updateBytes(String columnLabel, byte[] x) throws SQLException {
		this.resultSet.updateBytes(columnLabel, x);
	}

	@Override
	public void updateDate(String columnLabel, Date x) throws SQLException {
		this.resultSet.updateDate(columnLabel, x);
	}

	@Override
	public void updateTime(String columnLabel, Time x) throws SQLException {
		this.resultSet.updateTime(columnLabel, x);
	}

	@Override
	public void updateTimestamp(String columnLabel, Timestamp x)
		throws SQLException {
		this.resultSet.updateTimestamp(columnLabel, x);
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, int length)
		throws SQLException {
		this.resultSet.updateAsciiStream(columnLabel, x, length);
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x, int length)
		throws SQLException {
		this.resultSet.updateBinaryStream(columnLabel, x, length);
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader,
	                                  int length) throws SQLException {
		this.resultSet.updateCharacterStream(columnLabel, reader, length);
	}

	@Override
	public void updateObject(String columnLabel, Object x, int scaleOrLength)
		throws SQLException {
		this.resultSet.updateObject(columnLabel, x, scaleOrLength);
	}

	@Override
	public void updateObject(String columnLabel, Object x) throws SQLException {
		this.resultSet.updateObject(columnLabel, x);
	}

	@Override
	public void insertRow() throws SQLException {
		this.resultSet.insertRow();
	}

	@Override
	public void updateRow() throws SQLException {
		this.resultSet.updateRow();
	}

	@Override
	public void deleteRow() throws SQLException {
		this.resultSet.deleteRow();
	}

	@Override
	public void refreshRow() throws SQLException {
		this.resultSet.refreshRow();
	}

	@Override
	public void cancelRowUpdates() throws SQLException {
		this.resultSet.cancelRowUpdates();
	}

	@Override
	public void moveToInsertRow() throws SQLException {
		this.resultSet.moveToInsertRow();
	}

	@Override
	public void moveToCurrentRow() throws SQLException {
		this.resultSet.moveToCurrentRow();
	}

	@Override
	public Statement getStatement() throws SQLException {
		return this.resultSet.getStatement();
	}

	@Override
	public Object getObject(int columnIndex, Map<String, Class<?>> map)
		throws SQLException {
		return this.resultSet.getObject(columnIndex, map);
	}

	@Override
	public Ref getRef(int columnIndex) throws SQLException {
		return this.resultSet.getRef(columnIndex);
	}

	@Override
	public Blob getBlob(int columnIndex) throws SQLException {
		return this.resultSet.getBlob(columnIndex);
	}

	@Override
	public Clob getClob(int columnIndex) throws SQLException {
		return this.resultSet.getClob(columnIndex);
	}

	@Override
	public Array getArray(int columnIndex) throws SQLException {
		return this.resultSet.getArray(columnIndex);
	}

	@Override
	public Object getObject(String columnLabel, Map<String, Class<?>> map)
		throws SQLException {
		return this.resultSet.getObject(columnLabel, map);
	}

	@Override
	public Ref getRef(String columnLabel) throws SQLException {
		return this.resultSet.getRef(columnLabel);
	}

	@Override
	public Blob getBlob(String columnLabel) throws SQLException {
		return this.resultSet.getBlob(columnLabel);
	}

	@Override
	public Clob getClob(String columnLabel) throws SQLException {
		return this.resultSet.getClob(columnLabel);
	}

	@Override
	public Array getArray(String columnLabel) throws SQLException {
		return this.resultSet.getArray(columnLabel);
	}

	@Override
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		return this.resultSet.getDate(columnIndex, cal);
	}

	@Override
	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		return this.resultSet.getDate(columnLabel, cal);
	}

	@Override
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		return this.resultSet.getTime(columnIndex, cal);
	}

	@Override
	public Time getTime(String columnLabel, Calendar cal) throws SQLException {
		return this.resultSet.getTime(columnLabel, cal);
	}

	@Override
	public Timestamp getTimestamp(int columnIndex, Calendar cal)
		throws SQLException {
		return this.resultSet.getTimestamp(columnIndex, cal);
	}

	@Override
	public Timestamp getTimestamp(String columnLabel, Calendar cal)
		throws SQLException {
		return this.resultSet.getTimestamp(columnLabel, cal);
	}

	@Override
	public URL getURL(int columnIndex) throws SQLException {
		return this.resultSet.getURL(columnIndex);
	}

	@Override
	public URL getURL(String columnLabel) throws SQLException {
		return this.resultSet.getURL(columnLabel);
	}

	@Override
	public void updateRef(int columnIndex, Ref x) throws SQLException {
		this.resultSet.updateRef(columnIndex, x);
	}

	@Override
	public void updateRef(String columnLabel, Ref x) throws SQLException {
		this.resultSet.updateRef(columnLabel, x);
	}

	@Override
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		this.resultSet.updateBlob(columnIndex, x);
	}

	@Override
	public void updateBlob(String columnLabel, Blob x) throws SQLException {
		this.resultSet.updateBlob(columnLabel, x);
	}

	@Override
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		this.resultSet.updateClob(columnIndex, x);
	}

	@Override
	public void updateClob(String columnLabel, Clob x) throws SQLException {
		this.resultSet.updateClob(columnLabel, x);
	}

	@Override
	public void updateArray(int columnIndex, Array x) throws SQLException {
		this.resultSet.updateArray(columnIndex, x);
	}

	@Override
	public void updateArray(String columnLabel, Array x) throws SQLException {
		this.resultSet.updateArray(columnLabel, x);
	}

	@Override
	public RowId getRowId(int columnIndex) throws SQLException {
		return this.resultSet.getRowId(columnIndex);
	}

	@Override
	public RowId getRowId(String columnLabel) throws SQLException {
		return this.resultSet.getRowId(columnLabel);
	}

	@Override
	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		this.resultSet.updateRowId(columnIndex, x);
	}

	@Override
	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		this.resultSet.updateRowId(columnLabel, x);
	}

	@Override
	public int getHoldability() throws SQLException {
		return this.resultSet.getHoldability();
	}

	@Override
	public boolean isClosed() throws SQLException {
		return this.resultSet.isClosed();
	}

	@Override
	public void updateNString(int columnIndex, String nString)
		throws SQLException {
		this.resultSet.updateNString(columnIndex, nString);
	}

	@Override
	public void updateNString(String columnLabel, String nString)
		throws SQLException {
		this.resultSet.updateNString(columnLabel, nString);
	}

	@Override
	public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
		this.resultSet.updateNClob(columnIndex, nClob);
	}

	@Override
	public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
		this.resultSet.updateNClob(columnLabel, nClob);
	}

	@Override
	public NClob getNClob(int columnIndex) throws SQLException {
		return this.resultSet.getNClob(columnIndex);
	}

	@Override
	public NClob getNClob(String columnLabel) throws SQLException {
		return this.resultSet.getNClob(columnLabel);
	}

	@Override
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		return this.resultSet.getSQLXML(columnIndex);
	}

	@Override
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		return this.resultSet.getSQLXML(columnLabel);
	}

	@Override
	public void updateSQLXML(int columnIndex, SQLXML xmlObject)
		throws SQLException {
		this.resultSet.updateSQLXML(columnIndex, xmlObject);
	}

	@Override
	public void updateSQLXML(String columnLabel, SQLXML xmlObject)
		throws SQLException {
		this.resultSet.updateSQLXML(columnLabel, xmlObject);
	}

	@Override
	public String getNString(int columnIndex) throws SQLException {
		return this.resultSet.getNString(columnIndex);
	}

	@Override
	public String getNString(String columnLabel) throws SQLException {
		return this.resultSet.getNString(columnLabel);
	}

	@Override
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		return this.resultSet.getNCharacterStream(columnIndex);
	}

	@Override
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		return this.resultSet.getNCharacterStream(columnLabel);
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x, long length)
		throws SQLException {
		this.resultSet.updateNCharacterStream(columnIndex, x);
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader,
	                                   long length) throws SQLException {
		this.resultSet.updateNCharacterStream(columnLabel, reader, length);
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, long length)
		throws SQLException {
		this.resultSet.updateAsciiStream(columnIndex, x, length);
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, long length)
		throws SQLException {
		this.resultSet.updateBinaryStream(columnIndex, x, length);
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, long length)
		throws SQLException {
		this.resultSet.updateCharacterStream(columnIndex, x, length);
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, long length)
		throws SQLException {
		this.resultSet.updateAsciiStream(columnLabel, x, length);
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x, long length)
		throws SQLException {
		this.resultSet.updateBinaryStream(columnLabel, x, length);
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader,
	                                  long length) throws SQLException {
		this.resultSet.updateCharacterStream(columnLabel, reader, length);
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream, long length)
		throws SQLException {
		this.resultSet.updateBlob(columnIndex, inputStream, length);
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream,
	                       long length) throws SQLException {
		this.resultSet.updateBlob(columnLabel, inputStream, length);
	}

	@Override
	public void updateClob(int columnIndex, Reader reader, long length)
		throws SQLException {
		this.resultSet.updateClob(columnIndex, reader, length);
	}

	@Override
	public void updateClob(String columnLabel, Reader reader, long length)
		throws SQLException {
		this.resultSet.updateClob(columnLabel, reader, length);
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader, long length)
		throws SQLException {
		this.resultSet.updateNClob(columnIndex, reader, length);
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader, long length)
		throws SQLException {
		this.resultSet.updateNClob(columnLabel, reader, length);
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x)
		throws SQLException {
		this.resultSet.updateNCharacterStream(columnIndex, x);
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader)
		throws SQLException {
		this.resultSet.updateNCharacterStream(columnLabel, reader);
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x)
		throws SQLException {
		this.resultSet.updateAsciiStream(columnIndex, x);
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x)
		throws SQLException {
		this.resultSet.updateBinaryStream(columnIndex, x);
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x)
		throws SQLException {
		this.resultSet.updateCharacterStream(columnIndex, x);
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x)
		throws SQLException {
		this.resultSet.updateAsciiStream(columnLabel, x);
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x)
		throws SQLException {
		this.resultSet.updateBinaryStream(columnLabel, x);
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader)
		throws SQLException {
		this.resultSet.updateCharacterStream(columnLabel, reader);
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream)
		throws SQLException {
		this.resultSet.updateBlob(columnIndex, inputStream);
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream)
		throws SQLException {
		this.resultSet.updateBlob(columnLabel, inputStream);
	}

	@Override
	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		this.resultSet.updateClob(columnIndex, reader);
	}

	@Override
	public void updateClob(String columnLabel, Reader reader) throws SQLException {
		this.resultSet.updateClob(columnLabel, reader);
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		this.resultSet.updateNClob(columnIndex, reader);
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader)
		throws SQLException {
		this.resultSet.updateNClob(columnLabel, reader);
	}

	@Override
	public <T> T getObject(int arg0, Class<T> arg1) throws SQLException {
		return this.resultSet.getObject(arg0, arg1);
	}

	@Override
	public <T> T getObject(String arg0, Class<T> arg1) throws SQLException {
		return this.resultSet.getObject(arg0, arg1);
	}

}
