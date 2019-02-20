package org.monet.space.mobile.control.actions;

import org.monet.mobile.exceptions.ActionException;
import org.monet.mobile.service.Request;
import org.monet.mobile.service.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class TypedAction<T extends Request, U extends Result> extends Action<U> {

	private Class<T> requestClass;

	public TypedAction(Class<T> requestClass) {
		this.requestClass = requestClass;
	}

	@Override
	public U execute(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
		T request = getRequest(httpRequest, this.requestClass);
		return onExecute(request);
	}

	protected abstract U onExecute(T request) throws ActionException;

}
