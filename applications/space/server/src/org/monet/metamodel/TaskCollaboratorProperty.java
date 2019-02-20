package org.monet.metamodel;

import java.util.LinkedHashMap;

/**
 * TaskCollaboratorProperty
 */

public class TaskCollaboratorProperty extends ReferenceableProperty {

	protected org.monet.metamodel.internal.Ref _task;

	public org.monet.metamodel.internal.Ref getTask() {
		return _task;
	}

	public void setTask(org.monet.metamodel.internal.Ref value) {
		_task = value;
	}

	public static class RequestProperty extends org.monet.metamodel.TaskRequestProperty {
		protected void merge(RequestProperty child) {
			super.merge(child);
		}
	}

	protected LinkedHashMap<String, RequestProperty> _requestPropertyMap = new LinkedHashMap<String, RequestProperty>();

	public void addRequest(RequestProperty value) {
		String key = value.getName() != null ? value.getName() : value.getCode();
		RequestProperty current = _requestPropertyMap.get(key);
		if (current != null) {
			current.merge(value);
		} else {
			_requestPropertyMap.put(key, value);
		}
	}

	public java.util.Map<String, RequestProperty> getRequestMap() {
		return _requestPropertyMap;
	}

	public java.util.Collection<RequestProperty> getRequestList() {
		return _requestPropertyMap.values();
	}

	public static class ResponseProperty extends org.monet.metamodel.TaskResponseProperty {
		protected void merge(ResponseProperty child) {
			super.merge(child);
		}
	}

	protected LinkedHashMap<String, ResponseProperty> _responsePropertyMap = new LinkedHashMap<String, ResponseProperty>();

	public void addResponse(ResponseProperty value) {
		String key = value.getName() != null ? value.getName() : value.getCode();
		ResponseProperty current = _responsePropertyMap.get(key);
		if (current != null) {
			current.merge(value);
		} else {
			_responsePropertyMap.put(key, value);
		}
	}

	public java.util.Map<String, ResponseProperty> getResponseMap() {
		return _responsePropertyMap;
	}

	public java.util.Collection<ResponseProperty> getResponseList() {
		return _responsePropertyMap.values();
	}

	public static class AbortedProperty {
		protected org.monet.metamodel.internal.Ref _unlock;

		public org.monet.metamodel.internal.Ref getUnlock() {
			return _unlock;
		}

		public void setUnlock(org.monet.metamodel.internal.Ref value) {
			_unlock = value;
		}

		protected org.monet.metamodel.internal.Ref _goto;

		public org.monet.metamodel.internal.Ref getGoto() {
			return _goto;
		}

		public void setGoto(org.monet.metamodel.internal.Ref value) {
			_goto = value;
		}

		protected void merge(AbortedProperty child) {
			if (child._unlock != null)
				this._unlock = child._unlock;
			if (child._goto != null)
				this._goto = child._goto;
		}
	}

	protected AbortedProperty _abortedProperty;

	public AbortedProperty getAborted() {
		return _abortedProperty;
	}

	public void setAborted(AbortedProperty value) {
		if (_abortedProperty != null)
			_abortedProperty.merge(value);
		else {
			_abortedProperty = value;
		}
	}

	public void merge(TaskCollaboratorProperty child) {
		super.merge(child);

		if (child._task != null)
			this._task = child._task;

		for (RequestProperty item : child._requestPropertyMap.values())
			this.addRequest(item);
		for (ResponseProperty item : child._responsePropertyMap.values())
			this.addResponse(item);
		if (_abortedProperty == null)
			_abortedProperty = child._abortedProperty;
		else {
			_abortedProperty.merge(child._abortedProperty);
		}

	}

	public Class<?> getMetamodelClass() {
		return TaskCollaboratorProperty.class;
	}

}
