package client.services.http.builders;

import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.Report;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import com.google.gwt.core.client.JsArray;

public class ReportBuilder implements Builder<client.core.model.Report, List<client.core.model.Report>> {
	@Override
	public client.core.model.Report build(HttpInstance instance) {
		if (instance == null)
			return null;

		Report report = new Report();
		initialize(report, instance);
		return report;
	}

	@Override
	public void initialize(client.core.model.Report object, HttpInstance instance) {
		Report report = (Report)object;
		JsArray<HttpInstance> values = instance.getArray("values");

		for (int i = 0; i < values.length(); i++) {
			HttpInstance value = values.get(i);
			report.add(value.getString("code"), value.getInt("value"));
		}
	}

	@Override
	public List<client.core.model.Report> buildList(HttpList instances) {
		return new MonetList<>();
	}
}
