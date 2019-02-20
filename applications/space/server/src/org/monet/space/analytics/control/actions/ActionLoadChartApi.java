package org.monet.space.analytics.control.actions;

import org.monet.space.analytics.providers.Provider;
import org.monet.space.analytics.providers.ProvidersFactory;
import org.monet.space.analytics.configuration.Configuration;
import org.monet.space.analytics.constants.Parameter;
import org.monet.space.analytics.model.ChartType;
import org.monet.space.kernel.agents.AgentLogger;

public class ActionLoadChartApi extends Action {

	public ActionLoadChartApi() {
		super();
	}

	@Override
	public String execute() {
		String result = "";

		if (!this.isLogged())
			return this.launchAuthenticateAction();

		try {
			String chartType = this.getParameterAsString(Parameter.TYPE);
			String providerName = Configuration.getInstance().getProvider(ChartType.fromString(chartType));
			Provider provider = ProvidersFactory.Instance().get(providerName);

			result = provider.getChartApi();

		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}

		return result;
	}

}
