package org.monet.space.analytics.control.actions;

import org.monet.space.analytics.constants.Parameter;
import org.monet.space.analytics.control.constants.Commands;
import org.monet.space.analytics.renders.DatawareHouseRender;
import org.monet.space.analytics.renders.RendersFactory;
import org.monet.space.analytics.utils.AjaxCommandUtil;
import org.monet.space.kernel.components.ComponentDatawareHouse;
import org.monet.space.kernel.components.layers.DashboardLayer;
import org.monet.templation.Render;

public class ActionShowDashboard extends Action {
	private DashboardLayer dashboardLayer;

	public ActionShowDashboard() {
		super();
		this.dashboardLayer = ComponentDatawareHouse.getInstance().getDashboardLayer();
	}

	@Override
	public String execute() {
		RendersFactory rendersFactory = RendersFactory.getInstance();
		Render render;

		if (!this.isLogged())
			return this.launchAuthenticateAction();

		String dashboardId = this.getParameterAsString(Parameter.ID);
		String reportId = this.getParameterAsString(Parameter.REPORT);
		String modeId = this.getParameterAsString(Parameter.MODE);
		String[] parameters = new String[]{dashboardId, reportId, modeId};

		render = rendersFactory.get(RendersFactory.RENDER_APPLICATION);
		render.setParameter(DatawareHouseRender.Parameter.COMMAND, AjaxCommandUtil.constructCommand(Commands.SHOW_DASHBOARD, parameters));

		return render.getOutput();
	}

}
