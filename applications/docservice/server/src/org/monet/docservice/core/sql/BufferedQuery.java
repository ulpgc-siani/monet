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

package org.monet.docservice.core.sql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

public class BufferedQuery extends BufferedReader {
	private ArrayList<String> queries;
	private String sSubQuery;
	private Integer iBufferSize;

	public BufferedQuery(Reader oReader) {
		super(oReader);
		this.queries = new ArrayList<String>();
		this.sSubQuery = null;
		this.iBufferSize = 1024;
	}

	public BufferedQuery(ArrayList<String> queries) {
		super(new StringReader(""));
		this.queries = queries;
		this.sSubQuery = null;
		this.iBufferSize = 1024;
	}

	private String readBlockData() throws IOException {
		char[] aBuffer = new char[this.iBufferSize];
		Integer iResult;

		try {
			iResult = this.read(aBuffer);
			if (iResult == -1) return null;
		} catch (IOException oException) {
			throw oException;
		}

		return String.valueOf(aBuffer);
	}

	public String readQuery() throws IOException {
		String sQuery;
		Integer iPos;
		String sBlockData;
		String[] aQueries;

		if (this.queries.size() > 0) {
			sQuery = this.queries.get(0);
			this.queries.remove(0);
			return sQuery;
		}

		if ((sBlockData = this.readBlockData()) == null) return null;

		iPos = sBlockData.indexOf(";");
		while (iPos == -1) {
			String sNewBlockData = this.readBlockData();
			if (sBlockData == null) break;
			sBlockData += sNewBlockData;
			iPos = sBlockData.indexOf(";");
		}

		aQueries = sBlockData.split(";");
		if (this.sSubQuery != null) {
			aQueries[0] = this.sSubQuery + aQueries[0];
			this.sSubQuery = null;
		}

		for (iPos = 0; iPos < aQueries.length; iPos++) {
			if (aQueries[iPos].length() == 0) continue;
			if ((iPos == aQueries.length - 1) && (!sBlockData.substring(sBlockData.length() - 1, sBlockData.length()).equals(";"))) {
				this.sSubQuery = aQueries[iPos];
			} else this.queries.add(aQueries[iPos]);
		}

		return this.readQuery();
	}

}