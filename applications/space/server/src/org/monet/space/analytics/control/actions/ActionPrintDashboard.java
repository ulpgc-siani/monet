package org.monet.space.analytics.control.actions;

import org.monet.space.analytics.renders.DatawareHouseRender;
import org.monet.space.analytics.constants.Parameter;
import org.monet.space.analytics.control.constants.Commands;
import org.monet.space.analytics.renders.RendersFactory;
import org.monet.space.analytics.utils.AjaxCommandUtil;
import org.monet.templation.Render;

public class ActionPrintDashboard extends Action {

	public ActionPrintDashboard() {
		super();
	}

	@Override
	public String execute() {
		RendersFactory rendersFactory = RendersFactory.getInstance();
		Render render;

		if (!this.isLogged())
			return this.launchAuthenticateAction();

		String dashboardId = this.getParameterAsString(Parameter.ID);
		String reportId = this.getParameterAsString(Parameter.REPORT);
		String chartType = this.getParameterAsString(Parameter.TYPE);
		String indicators = this.getParameterAsString(Parameter.INDICATORS);
		String compare = this.getParameterAsString(Parameter.COMPARE);
		String filters = this.getParameterAsString(Parameter.FILTERS);
		String ranges = this.getParameterAsString(Parameter.RANGES);
		String fromValue = this.getParameterAsString(Parameter.FROM);
		String toValue = this.getParameterAsString(Parameter.TO);
		String scale = this.getParameterAsString(Parameter.SCALE);
		String colors = this.getParameterAsString(Parameter.COLORS);
		String[] parameters = new String[]{dashboardId, reportId, chartType, indicators, compare, filters, ranges, fromValue, toValue, scale, colors};

		render = rendersFactory.get(RendersFactory.RENDER_APPLICATION);
		render.setParameter(DatawareHouseRender.Parameter.COMMAND, AjaxCommandUtil.constructCommand(Commands.PRINT_DASHBOARD, parameters));

		return render.getOutput();
	}

}
