package client.services.http;

import com.google.gwt.core.client.JsArray;

public class HttpList extends HttpInstance {

	protected HttpList() {
	}

	public final native int getTotalCount() /*-{
		return this["totalCount"];
	}-*/;

	public final native JsArray<HttpInstance> getItems() /*-{
		return this["items"];
	}-*/;

	public final native String getItemAtPosAsString(int pos) /*-{
		return this["items"][pos];
	}-*/;

}
