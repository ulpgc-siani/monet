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

package org.monet.space.applications.library;

import org.monet.space.kernel.agents.AgentLogger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class LibrarySerializer64 {

	private static final String DATA = "tQ0CS4GmaIAO1XVgrqdkinM7$8Y9phNRDj6JczExvUeTWKHL5.2usBlw3fbFyoZP";

	public static Integer readInt(String sData) {
		Integer iIndex, iResult = 0;
		Integer iPos = sData.length() - 1;

		while (iPos >= 0) {
			iIndex = DATA.indexOf(sData.charAt(iPos));
			iResult = iResult * 64 + iIndex;
			iPos--;
		}

		return iResult;
	}

	public static String writeInt(Integer iValue, Integer iDigits) {
		String sResult = "";
		Integer iIndex;

		if (iValue == 0 && (iDigits == 0 || iDigits == null)) return DATA.substring(0, 1);

		while (iValue > 0 || iDigits > 0) {
			iIndex = iValue % 64;
			sResult = sResult + DATA.charAt(iIndex);
			iValue = (int) Math.floor(iValue / 64);
			iDigits--;
		}

		return sResult;
	}

	public static String readString(String sData) {
		String sResult = "";
		Integer iPos, iCode, iIndex;

		for (iPos = 0; iPos < sData.length(); iPos += 4) {
			iCode = LibrarySerializer64.readInt(sData.substring(iPos, iPos + 4));
			sResult += (char) ((iCode >> 16) & 0xFF);
			sResult += (char) ((iCode >> 8) & 0xFF);
			sResult += (char) (iCode & 0xFF);
		}

		iIndex = sResult.length();
		while (iIndex > 0 && (sResult.charAt(iIndex - 1) == ' ')) iIndex--;
		sResult = sResult.substring(0, iIndex);

		try {
			sResult = URLDecoder.decode(sResult, "UTF-8");
		} catch (UnsupportedEncodingException oException) {
			AgentLogger.getInstance().error(oException);
			return null;
		}

		return sResult;
	}

	public static String writeString(String sValue) {
		String sResult = "";
		Integer iExtra, iPos;
		String[] aSpaces = {"", "  ", " "};
		Integer iCode;

		try {
			sValue = URLEncoder.encode(sValue, "UTF-8");
		} catch (UnsupportedEncodingException oException) {
			AgentLogger.getInstance().error(oException);
			return null;
		}

		iExtra = sValue.length() % 3;
		sValue += aSpaces[iExtra];

		for (iPos = 0; iPos < sValue.length(); iPos += 3) {
			iCode = (sValue.charAt(iPos) << 16) + (sValue.charAt(iPos + 1) << 8) + sValue.charAt(iPos + 2);
			sResult = sResult + LibrarySerializer64.writeInt(iCode, 4);
		}

		return sResult;
	}

}