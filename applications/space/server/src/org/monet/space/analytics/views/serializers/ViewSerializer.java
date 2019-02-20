package org.monet.space.analytics.views.serializers;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

@Root(name = "asset")
public class ViewSerializer {

	@ElementList(name = "show", inline = true, required = false)
	private ArrayList<ShowXml> showList = new ArrayList<ShowXml>();

	public ArrayList<ShowXml> getShowList() {
		return showList;
	}

	public void setShowList(ArrayList<ShowXml> showList) {
		this.showList = showList;
	}

	@Root(name = "show")
	public static class ShowXml {

		@Attribute(required = true)
		private String indicator;

		public String getIndicator() {
			return indicator;
		}

		public void setIndicator(String indicator) {
			this.indicator = indicator;
		}
	}

}
