package org.monet.space.analytics.control.actions;

import org.monet.space.analytics.model.Kernel;
import org.monet.space.analytics.renders.DatawareHouseRender;
import org.monet.space.analytics.constants.Parameter;
import org.monet.space.analytics.renders.RendersFactory;
import org.monet.space.analytics.serializers.IndicatorsSerializer;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Dashboard;
import org.monet.templation.Render;
import org.sumus.asset.Asset;
import org.sumus.asset.AssetException;
import org.sumus.indicator.IndicatorList;
import org.sumus.report.Report;

public class ActionLoadDashboard extends Action {

	public ActionLoadDashboard() {
		super();
	}

	@Override
	public String execute() {

		if (!this.isLogged())
			return this.launchAuthenticateAction();

		String dashboardId = this.getParameterAsString(Parameter.ID);
		String reportId = this.getParameterAsString(Parameter.REPORT);
		String indicators = this.getParameterAsString(Parameter.INDICATORS);
		IndicatorList indicatorList = IndicatorsSerializer.fromJson(indicators);
		Kernel kernel;
		Dashboard dashboard;

		try {
			kernel = Kernel.Instance();

			if (!kernel.isDashboardLoaded(dashboardId))
				return null;

			dashboard = kernel.getDashboard(dashboardId);
			this.loadDashboardModel(dashboard, reportId, indicatorList);

			return dashboard.toJson().toString();
		} catch (AssetException exception) {
			AgentLogger.getInstance().error(exception);
			throw new DataException("load dashboard", dashboardId, exception);
		}

	}

	public void loadDashboardModel(Dashboard dashboard, String reportId, IndicatorList indicatorList) throws AssetException {
		Asset asset = (Asset) dashboard.getAsset();
		Report report = asset.getReport(reportId);
		IndicatorList kernelIndicatorList = this.getSumusIndicators(asset, indicatorList);

		if (report == null) {
			Report[] reports = asset.getReports();
			report = reports.length > 0 ? reports[0] : null;
		}

		Render render = RendersFactory.getInstance().get(RendersFactory.RENDER_DASHBOARD_MODEL);
		render.setParameter(DatawareHouseRender.Parameter.SELECTED_INDICATORS, kernelIndicatorList);
		render.setParameter(DatawareHouseRender.Parameter.REPORT, report);
		render.setTarget(dashboard);

		dashboard.setModel(render.getOutput());
	}


}
