package org.monet.space.explorer.control.displays;

import org.monet.space.explorer.control.displays.serializers.ReportSerializer;
import org.monet.space.explorer.control.displays.serializers.Serializer;
import org.monet.space.explorer.model.Report;

public class ReportDisplay extends HttpDisplay<Report> {

	@Override
	protected Serializer getSerializer(Report object) {
		return new ReportSerializer(createSerializerHelper());
	}

}
