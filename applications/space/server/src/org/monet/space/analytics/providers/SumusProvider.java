package org.monet.space.analytics.providers;

import org.monet.space.analytics.model.RangeList;
import org.monet.space.analytics.providers.renders.RenderProvidersFactory;
import org.monet.space.analytics.configuration.Configuration;
import org.monet.space.analytics.model.Language;
import org.monet.space.analytics.providers.renders.DatawareHouseProviderRender;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.model.Dashboard;
import org.monet.templation.Render;
import org.sumus.dimension.category.CategoryList;
import org.sumus.indicator.IndicatorList;
import org.sumus.query.Chart;

import java.util.HashMap;

public class SumusProvider implements Provider {

	@Override
	public String getChartApi() {
		String language = Language.getCurrent();
		String filename = Configuration.getInstance().getTemplatesProvidersDir(language) + "/sumuschartapi.tpl";

		if (!AgentFilesystem.existFile(filename))
			throw new RuntimeException(String.format("sumus provider client api file not found on templates/%s/providers directory", language));

		return AgentFilesystem.readFile(filename);
	}

	@Override
	public String getChartData(Dashboard dashboard, int chartType, Chart chart, IndicatorList indicatorList, RangeList rangeList) {
		Render render = RenderProvidersFactory.Instance().get(RenderProvidersFactory.SUMUS_CHART);
		render.setParameter(DatawareHouseProviderRender.Parameter.CHART_TYPE, chartType);
		render.setParameter(DatawareHouseProviderRender.Parameter.INDICATOR_LIST, indicatorList);
		render.setParameter(DatawareHouseProviderRender.Parameter.RANGE_LIST, rangeList);
		render.setParameter(DatawareHouseProviderRender.Parameter.DASHBOARD, dashboard);
		render.setTarget(chart);
		return render.getOutput();
	}

	@Override
	public String getDocumentData(Dashboard dashboard, int format, Chart chart, IndicatorList indicatorList, RangeList rangeList, HashMap<String, CategoryList> filters) {
		Render render = RenderProvidersFactory.Instance().get(RenderProvidersFactory.SUMUS_DOCUMENT);
		render.setParameter(DatawareHouseProviderRender.Parameter.FORMAT, format);
		render.setParameter(DatawareHouseProviderRender.Parameter.INDICATOR_LIST, indicatorList);
		render.setParameter(DatawareHouseProviderRender.Parameter.RANGE_LIST, rangeList);
		render.setParameter(DatawareHouseProviderRender.Parameter.FILTERS, filters);
		render.setParameter(DatawareHouseProviderRender.Parameter.DASHBOARD, dashboard);
		render.setTarget(chart);
		return render.getOutput();
	}

}