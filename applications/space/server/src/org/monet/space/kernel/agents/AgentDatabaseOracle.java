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

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKBWriter;
import oracle.spatial.geometry.JGeometry;
import oracle.spatial.util.WKB;
import oracle.sql.STRUCT;
import org.apache.tomcat.dbcp.dbcp.DelegatingConnection;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AgentDatabaseOracle extends AgentDatabase {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	public AgentDatabaseOracle() {
		registerType(AgentDatabase.ColumnTypes.DATE, "TIMESTAMP(6)");
		registerType(AgentDatabase.ColumnTypes.FLOAT, "FLOAT(11)");
		registerType(AgentDatabase.ColumnTypes.INTEGER, "NUMBER(11)");
		registerType(AgentDatabase.ColumnTypes.BOOLEAN, "NUMBER");
		registerType(AgentDatabase.ColumnTypes.TEXT, "VARCHAR2(4000 BYTE)");
		registerType("default", "VARCHAR2(4000 BYTE)");
	}

	public Timestamp getDateAsTimestamp(Date date) {
		return date != null ? new Timestamp(date.getTime()) : null;
	}

	public Object getDateAsMilliseconds(Date date) {
		return date != null ? new Timestamp(date.getTime()) : null;
	}

	public Date parseDateInMilliseconds(ResultSet resultSet, String columnName) throws SQLException {
		return resultSet.getTimestamp(columnName);
	}

	public Geometry getGeometryColumn(ResultSet resultSet, String column) throws Exception {
		STRUCT struct = (oracle.sql.STRUCT) resultSet.getObject(column);

		if (struct == null || resultSet.wasNull())
			return null;

		WKB wkb = new WKB();
		byte[] wkbRaw = wkb.fromSTRUCT(struct);

		WKBReader wkbReader = new WKBReader();
		Geometry geometry = wkbReader.read(wkbRaw);
		geometry.setSRID(JGeometry.load(struct).getSRID());
		return geometry;
	}

	public Object fromGeometry(Connection connection, Geometry geometry) throws Exception {
		Connection oracleConnection = connection;

		if (connection instanceof DelegatingConnection) {
			oracleConnection = ((DelegatingConnection) connection).getInnermostDelegate();
		}

		WKBWriter writer = new WKBWriter();
		byte[] wkbRaw = writer.write(geometry);
		WKB wkb = new WKB();
		STRUCT obj = wkb.toSTRUCT(wkbRaw, oracleConnection);
		JGeometry g = JGeometry.load(obj);
		g.setSRID(geometry.getSRID());
		obj = JGeometry.store(oracleConnection, g);
		return obj;
	}

	@Override
	public String getEmptyString() {
		return "-";
	}

	public String getDateAsText(Date dtDate) {
		if (dtDate == null)
			return "\'\'";
		return "to_date('" + DATE_FORMAT.format(dtDate) + "','DD-MM-YYYY HH24:MI:SS')";
	}

	public boolean isValid(Connection connection) {
		boolean result;
		Statement statement;

		try {
			statement = connection.createStatement();
			result = statement.execute("SELECT 1 FROM dual");
		} catch (SQLException exception) {
			return false;
		}

		return result;
	}

	public int getQueryStartPos(int startPos) {
		return ++startPos;
	}

	@Override
	public String prepareAsFullTextCondition(String condition) {
		if (condition == null || condition.isEmpty() || condition.trim().isEmpty()) return "";

        condition = condition.toUpperCase();
		condition = condition.replaceAll(" (ABOUT|ACCUM|AND|BT|BTG|BTI|BTP|EQUIV|FUZZY|HASPATH|INPATH|MDATA|MINUS|NEAR|NOT|NT|NTG|NTI|NTP|OR|PT|RT|SQE|SYN|TR|TRSYN|TT|WITHIN) ", " ").replaceAll("(-|,|&|=|\\.|\\?|\\{|\\}|\\\\|\\(|\\)|\\[|\\]|;|~|\\||$|!|\\>|\\*|%|_)", " ").replaceAll("\\s+", " ").replaceAll("\\s[yY]\\s", "#AND#").replaceAll("\\s[oO]\\s", "#OR#").replaceAll("\\s", " AND ").replaceAll("#AND#", " AND ").replaceAll("#OR#", " OR ").replaceAll("(\\sOR(\\sAND\\s)OR\\s|\\sOR(\\sOR\\s)OR\\s)", "$2$3").trim().replaceAll("(^(OR|AND)|(OR|AND)$)", "").trim();
		condition = condition.replaceAll(" OR ", "% OR %").replaceAll(" AND ", "% AND %");
		condition = "(%" + condition + "%) OR (" + condition.replaceAll("%", "") + ")";

		return condition;
	}

	@Override
	public String getSchemaName() {
		return "1";
	}

}