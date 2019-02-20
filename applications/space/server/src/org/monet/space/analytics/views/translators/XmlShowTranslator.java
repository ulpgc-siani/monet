package org.monet.space.analytics.views.translators;

import org.monet.space.analytics.views.serializers.ViewSerializer;
import org.monet.space.analytics.model.Show;

public class XmlShowTranslator {

	public Show deserialize(Object serializedShow) {
		checkIsSerializedShow(serializedShow);
		return deserializeShow((ViewSerializer.ShowXml) serializedShow);
	}

	public Object serialize(Show show) {
		return serializeShow(show);
	}

	private void checkIsSerializedShow(Object serializedShow) {
		if (serializedShow instanceof ViewSerializer.ShowXml) return;
		throw new RuntimeException("serialized show is wrong");
	}

	private Show deserializeShow(ViewSerializer.ShowXml showXml) {
		try {
			return new Show(showXml.getIndicator());
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("Error deserializing the show");
		}
	}

	private ViewSerializer.ShowXml serializeShow(Show show) {
		try {
			ViewSerializer.ShowXml showXml = new ViewSerializer.ShowXml();
			showXml.setIndicator(show.getIndicator());
			return showXml;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("Error serializing the show");
		}
	}

}
