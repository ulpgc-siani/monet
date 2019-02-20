package org.monet.bpi.java;

import org.monet.bpi.Metric;

@SuppressWarnings("unused")
public class MeasureUnitImpl implements Metric {
	private org.monet.space.kernel.model.MeasureUnit measureUnit;

	void injectMeasureUnit(org.monet.space.kernel.model.MeasureUnit measureUnit) {
		this.measureUnit = measureUnit;
	}

}