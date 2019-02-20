package org.monet.space.analytics.control.actions;

import org.monet.space.analytics.model.Kernel;
import org.monet.space.analytics.model.RangeList;
import org.monet.space.analytics.providers.Provider;
import org.monet.space.analytics.providers.ProvidersFactory;
import org.monet.space.analytics.configuration.Configuration;
import org.monet.space.analytics.constants.Parameter;
import org.monet.space.analytics.model.ChartStore;
import org.monet.space.analytics.model.ChartType;
import org.monet.space.analytics.serializers.IndicatorsSerializer;
import org.monet.space.analytics.serializers.RangesSerializer;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.model.Dashboard;
import org.sumus.asset.Asset;
import org.sumus.indicator.Indicator;
import org.sumus.indicator.IndicatorList;
import org.sumus.query.Chart;
import org.sumus.time.TimeLapse;
import org.sumus.time.TimeScale;
import org.sumus.time.TimeStamp;

import java.util.Date;

public class ActionLoadChart extends Action {

	public ActionLoadChart() {
		super();
	}

	@Override
	public String execute() {
		String result = "";

		if (!this.isLogged())
			return this.launchAuthenticateAction();

		try {
			String dashboardId = this.getParameterAsString(Parameter.DASHBOARD);
			int chartType = ChartType.fromString(this.getParameterAsString(Parameter.TYPE));
			String indicators = this.getParameterAsString(Parameter.INDICATORS);
			String compare = this.getParameterAsString(Parameter.COMPARE);
			String filters = this.getParameterAsString(Parameter.FILTERS);
			String ranges = this.getParameterAsString(Parameter.RANGES);
			String providerName = Configuration.getInstance().getProvider(chartType);
			String fromValue = this.getParameterAsString(Parameter.FROM);
			String toValue = this.getParameterAsString(Parameter.TO);
			int scale = Integer.valueOf(this.getParameterAsString(Parameter.SCALE));
			Date from = new Date(), to = new Date();
			IndicatorList indicatorList = null;
			Provider provider = ProvidersFactory.Instance().get(providerName);
			Kernel instance = Kernel.Instance();
			Dashboard dashboard = instance.getDashboard(dashboardId);
			Asset asset = (Asset) dashboard.getAsset();
			IndicatorList kernelIndicatorList = new IndicatorList();
			RangeList rangeList = new RangeList();

			indicatorList = IndicatorsSerializer.fromJson(indicators);
			rangeList = RangesSerializer.fromJson(ranges);

			if (fromValue != null && !fromValue.isEmpty() && !fromValue.equals("null"))
				from = new Date(Long.parseLong(fromValue));

			if (toValue != null && !toValue.isEmpty() && !toValue.equals("null"))
				to = new Date(Long.parseLong(toValue));

			TimeStamp fromStamp = new TimeStamp(from, TimeScale.get(scale));
			TimeStamp toStamp = new TimeStamp(to, TimeScale.get(scale));
			TimeLapse timeLapse;
			Chart chart = null;

			try {
				timeLapse = this.getTimeLapse(fromStamp, toStamp, asset, indicatorList);
			} catch (Exception exception) {
				timeLapse = null;
			}

			if (timeLapse != null && !timeLapse.isEmpty()) {
				chart = asset.createChart(timeLapse);
                ChartStore.Instance().put(this.getSessionId(), chart);
				this.addChartSelection(chart, dashboardId, filters, indicatorList);
				this.addChartDrill(chart, dashboardId, compare, indicatorList);

				for (Indicator selectedIndicator : indicatorList) {
					Indicator indicator = instance.getIndicator(asset, selectedIndicator.getName());
					kernelIndicatorList.add(indicator);
					chart.addIndicator(indicator);
				}

				chart.refresh();
			}

			result = provider.getChartData(dashboard, chartType, chart, kernelIndicatorList, rangeList);

		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}

		return result;
	}

}