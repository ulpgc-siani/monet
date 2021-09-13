/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.space.kernel.agents;

import com.mysql.cj.jdbc.ConnectionImpl;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.*;
import org.monet.space.kernel.library.LibraryString;

import java.io.ByteArrayOutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class AgentDatabaseMysql extends AgentDatabase {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public AgentDatabaseMysql() {
		registerType(AgentDatabase.ColumnTypes.DATE, "DATETIME");
		registerType(AgentDatabase.ColumnTypes.FLOAT, "FLOAT(11)");
		registerType(AgentDatabase.ColumnTypes.INTEGER, "INT(11)");
		registerType(AgentDatabase.ColumnTypes.BOOLEAN, "INT(1)");
		registerType(AgentDatabase.ColumnTypes.TEXT, "TEXT COLLATE UTF8_UNICODE_CI");
		registerType("default", "TEXT COLLATE UTF8_UNICODE_CI");
	}

	public Timestamp getDateAsTimestamp(Date date) {
		return date != null ? new Timestamp(date.getTime()) : null;
	}

	public Object getDateAsMilliseconds(Date date) {
		return date.getTime() / 1000.0;
	}

	public Date parseDateInMilliseconds(ResultSet resultSet, String columnName) throws SQLException {
		Date result = new Date();
		Double value = resultSet.getDouble(columnName) * 1000.0;
		result.setTime(value.longValue());
		return result;
	}

	public Geometry getGeometryColumn(ResultSet resultSet, String column) throws Exception {
		byte[] mysqlGeometry = resultSet.getBytes(column);

		if (resultSet.wasNull())
			return null;

		return getGeometry(mysqlGeometry);
	}

	private Geometry getGeometry(byte[] mysqlGeometry) throws ParseException {
		Geometry geometry = geometryFromWellKnowBinary(mysqlGeometry);
		geometry.setSRID(srid(mysqlGeometry));
		return geometry;
	}

	private Geometry geometryFromWellKnowBinary(byte[] mysqlGeometry) throws ParseException {
		WKBReader wkbReader = new WKBReader();
		byte wkb[] = new byte[mysqlGeometry.length - 4];
		System.arraycopy(mysqlGeometry, 4, wkb, 0, mysqlGeometry.length - 4);
		return wkbReader.read(wkb);
	}

	private int srid(byte[] mysqlGeometry) {
		byte[] buf = new byte[4];
		System.arraycopy(mysqlGeometry, 0, buf, 0, 4);
		return ByteOrderValues.getInt(buf, ByteOrderValues.LITTLE_ENDIAN);
	}

	public Object fromGeometry(Connection connection, Geometry geometry) throws Exception {
		// create a variable byte array to write to
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		// write the 4-byte srid to byteOutputStream
		byte[] srid = new byte[4];
		ByteOrderValues.putInt(geometry.getSRID(), srid, ByteOrderValues.LITTLE_ENDIAN);
		byteOutputStream.write(srid);
		// write the geometry as WKB to byteOutputStream
		WKBWriter wkbWriter = new WKBWriter(2, ByteOrderValues.LITTLE_ENDIAN);
		wkbWriter.write(geometry, new OutputStreamOutStream(byteOutputStream));
		return byteOutputStream.toByteArray();
	}

	@Override
	public String getEmptyString() {
		return "";
	}

	public String getDateAsText(Date dtDate) {
		if (dtDate == null) return "null";
		return "\'" + DATE_FORMAT.format(dtDate) + "\'";
	}

	public boolean isValid(Connection connection) {
		try {
			Statement statement = connection.createStatement();
			return statement.execute("SELECT 1");
		} catch (SQLException oException) {
			return false;
		}
	}

	public int getQueryStartPos(int startPos) {
		return startPos;
	}

	@Override
	public String prepareAsFullTextCondition(String condition) {
		if (condition == null || condition.isEmpty() || condition.trim().isEmpty()) return "";

		condition = condition.replaceAll("\\s+", " ").replaceAll("\\s[oO]\\s", "#o#").replaceAll("\\s", " ").replaceAll("#o#", " ").replaceAll("ñ", "n").trim();
		String result = "";

		List<String> conditionList = this.getFullTextWords(condition);
		for (String conditionItem : conditionList) {

			if (result.length() > 0)
				result += " ";

			if (conditionItem.toLowerCase().equals("no"))
				result += "+(" + conditionItem.replaceAll("\\.", "") + " \"" + conditionItem + "\")";
			else
				result += "+(" + conditionItem.replaceAll("\\.", "") + "* \"" + conditionItem + "\"*)";
		}

		result = LibraryString.cleanAccents(result);

		return "( " + result + " )";
	}

	private ArrayList<String> getFullTextWords(String condition) {
		ArrayList<String> result = new ArrayList<String>();
		String[] conditionArray = condition.split(" ");

		for (int i=0; i<conditionArray.length; i++) {
			String conditionWord = conditionArray[i];
			conditionWord = addMasksToWord(conditionWord);
			String[] conditionWordArray = conditionWord.split("#.#");

			for (int j=0; j<conditionWordArray.length; j++) {
				if (conditionWordArray[j].isEmpty()) continue;
				result.add(conditionWordArray[j]);
			}
		}

		return result;
	}

	private String addMasksToWord(String conditionWord) {
		conditionWord = conditionWord.replace("<", "#<#");
		conditionWord = conditionWord.replace(">", "#>#");
		conditionWord = conditionWord.replace("+", "#+#");
		conditionWord = conditionWord.replace("-", "#-#");
		conditionWord = conditionWord.replace("/", "#/#");
		return conditionWord;
	}

	@Override
	public String getSchemaName() {
		String schemaName = "";
		Connection connection = null;

		try {
			connection = this.dataSource.getConnection();
			return connection.getCatalog();
		} catch (SQLException ex) {
		} finally {
			close(connection);
		}

		return schemaName;
	}

}