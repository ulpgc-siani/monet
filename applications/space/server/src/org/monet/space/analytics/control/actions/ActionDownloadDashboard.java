package org.monet.space.analytics.control.actions;

import org.monet.space.analytics.configuration.Configuration;
import org.monet.space.analytics.constants.Parameter;
import org.monet.space.analytics.model.Format;
import org.monet.space.analytics.model.Kernel;
import org.monet.space.analytics.model.RangeList;
import org.monet.space.analytics.providers.Provider;
import org.monet.space.analytics.providers.ProvidersFactory;
import org.monet.space.analytics.serializers.CategoriesSerializer;
import org.monet.space.analytics.serializers.IndicatorsSerializer;
import org.monet.space.analytics.serializers.RangesSerializer;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.library.LibraryDate;
import org.monet.space.kernel.library.LibraryString;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Dashboard;
import org.monet.space.kernel.utils.MimeTypes;
import org.sumus.asset.Asset;
import org.sumus.indicator.Indicator;
import org.sumus.indicator.IndicatorList;
import org.sumus.query.Chart;
import org.sumus.time.TimeLapse;
import org.sumus.time.TimeScale;
import org.sumus.time.TimeStamp;

import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;

public class ActionDownloadDashboard extends Action {

	public ActionDownloadDashboard() {
		super();
	}

	@Override
	public String execute() {

		if (!this.isLogged())
			return this.launchAuthenticateAction();

		String dashboardId = this.getParameterAsString(Parameter.ID);
		String indicators = this.getParameterAsString(Parameter.INDICATORS);
		String compare = this.getParameterAsString(Parameter.COMPARE);
		String filters = this.getParameterAsString(Parameter.FILTERS);
		String ranges = this.getParameterAsString(Parameter.RANGES);
		int format = Format.fromString(this.getParameterAsString(Parameter.FORMAT));
		String providerName = Configuration.getInstance().getProvider(format);
		String fromValue = this.getParameterAsString(Parameter.FROM);
		String toValue = this.getParameterAsString(Parameter.TO);
		int scale = Integer.valueOf(this.getParameterAsString(Parameter.SCALE));
		Date from = new Date(), to = new Date();
		IndicatorList indicatorList = null;
		Provider provider = ProvidersFactory.Instance().get(providerName);
		Kernel instance = Kernel.Instance();
		Asset asset = (Asset) instance.getDashboard(dashboardId).getAsset();
		IndicatorList kernelIndicatorList = new IndicatorList();
		RangeList rangeList = new RangeList();

		indicatorList = IndicatorsSerializer.fromJson(indicators);
		rangeList = RangesSerializer.fromJson(ranges);

		if (fromValue != null && !fromValue.isEmpty() && !fromValue.equals("null") && !fromValue.equals("0"))
			from = new Date(Long.parseLong(fromValue));

		if (toValue != null && !toValue.isEmpty() && !toValue.equals("null") && !toValue.equals("0"))
			to = new Date(Long.parseLong(toValue));

		Dashboard dashboard = instance.getDashboard(dashboardId);
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		TimeStamp fromStamp = from != null ? new TimeStamp(from, TimeScale.get(scale)) : new TimeStamp(currentYear - 1);
		TimeStamp toStamp = to != null ? new TimeStamp(to, TimeScale.get(scale)) : new TimeStamp(currentYear);
		TimeLapse timeLapse;
		Chart chart = null;
		String result = null;

		try {
			timeLapse = new TimeLapse(fromStamp, toStamp);
		} catch (Exception exception) {
			timeLapse = null;
		}

		try {
			if (timeLapse != null && !timeLapse.isEmpty()) {
				chart = asset.createChart(timeLapse);
				this.addChartSelection(chart, dashboardId, filters, indicatorList);
				this.addChartDrill(chart, dashboardId, compare, indicatorList);

				for (Indicator selectedIndicator : indicatorList) {
					Indicator indicator = instance.getIndicator(asset, selectedIndicator.getName());
					kernelIndicatorList.add(indicator);
					chart.addIndicator(indicator);
				}

				chart.refresh();
			}

			result = provider.getDocumentData(dashboard, format, chart, kernelIndicatorList, rangeList, CategoriesSerializer.fromJson(filters));
			String filename = dashboard.getLabel() + "_" + LibraryDate.getDateAndTimeString(new Date(), this.codeLanguage, BusinessUnit.getTimeZone(), LibraryDate.Format.NUMERIC, false, "_");
			filename = LibraryString.replaceAll(filename, " ", "_");
			byte[] resultBytes = result.getBytes("utf-8");

			HttpServletResponse response = this.getResponse();
			response.setContentLength(resultBytes.length);
			response.setContentType(MimeTypes.getInstance().get("xls"));
			response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");
			response.getOutputStream().write(resultBytes);
			response.getOutputStream().flush();

		} catch (Exception exception) {
			AgentLogger.getInstance().error(exception);
		}

		return null;
	}

}
