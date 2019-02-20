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

package org.monet.space.kernel.translators;

import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.exceptions.SystemException;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public class TranslatorsFactory {

	private static TranslatorsFactory oInstance;
	private HashMap<String, Class<?>> hmTranslators;

	private TranslatorsFactory() {
		this.hmTranslators = new HashMap<String, Class<?>>();
	}

	public synchronized static TranslatorsFactory getInstance() {
		if (oInstance == null) oInstance = new TranslatorsFactory();
		return oInstance;
	}

	public Object get(String sType) {
		Class<?> cTranslator;
		Translator oTranslator = null;

		try {
			cTranslator = (Class<?>) this.hmTranslators.get(sType);
			Constructor<?> oConstructor = cTranslator.getConstructor(String.class);
			oTranslator = (Translator) oConstructor.newInstance(sType);
		} catch (NullPointerException oException) {
			throw new SystemException(ErrorCode.TRANSLATORS_FACTORY, sType, oException);
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.TRANSLATORS_FACTORY, sType, oException);
		}

		return oTranslator;
	}

	public Boolean register(String sType, Class<?> cProducer)
		throws IllegalArgumentException {

		if ((cProducer == null) || (sType == null)) {
			return false;
		}
		this.hmTranslators.put(sType, cProducer);

		return true;
	}

}
