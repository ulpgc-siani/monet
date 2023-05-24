package org.monet.federation.accountoffice.core.components.templatecomponent.impl.renders;

import com.google.inject.Inject;
import org.monet.api.space.backservice.BackserviceApi;
import org.monet.api.space.backservice.impl.BackserviceApiImpl;
import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateComponent;
import org.monet.federation.accountoffice.core.components.templatecomponent.impl.FederationRender;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.BusinessUnit;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class BoardRender extends FederationRender {
	private String error;
	private List<BusinessUnit> units;
	private Logger logger;

	@Inject
	public void injectLogger(Logger logger) {
		this.logger = logger;
	}

	public BoardRender(Logger logger, Configuration configuration,
	                   String template) {
		super(logger, configuration, template);
	}

	@Override
	public void init(String lang) {
		this.lang = lang;
	}

	public void addMoreParams(List<BusinessUnit> units, String error) {
		this.units = units;
		this.error = error;
	}

	@Override
	protected void init() {
		Properties props = this.configuration.getLanguage(lang);
		String requestToken = this.getParameterAsString(Parameter.REQUEST_TOKEN);
		String baseUrl = this.getParameterAsString(Parameter.BASE_URL);

		loadCanvas(TemplateParams.CANVAS_BOARD);

		addMark(TemplateParams.ORGANITATION, props.getProperty(TemplateComponent.ORGANIZATION));
		addMark(TemplateParams.ABOUT, props.getProperty(TemplateComponent.ABOUT));
		addMark(TemplateParams.COPYRIGHT, this.getCopyright(props));
		addMark(TemplateParams.PRIVACY, props.getProperty(TemplateComponent.PRIVACY));
		addMark(TemplateParams.CLOSE, props.getProperty(TemplateComponent.CLOSE));
		addMark(TemplateParams.WELLCOME, props.getProperty(TemplateComponent.WELLCOME));
		addMark(TemplateParams.POWERED_BY, props.getProperty(TemplateComponent.POWERED_BY));
		addMark(TemplateParams.CHANGE_LANGUAGE, props.getProperty(TemplateComponent.CHANGE_LANGUAGE));
		addMark(TemplateParams.TOKEN, requestToken);
		addMark(TemplateParams.LOGO, baseUrl + this.configuration.getLogoPath());
		addMark(TemplateParams.URL_FEDERATION, baseUrl);
		addMark(TemplateParams.HOME, props.getProperty(TemplateComponent.HOME));
		addMark(TemplateParams.ACTION_HOME, baseUrl + "/accounts/authorization/home");

		String error = "";
		if (this.error != null)
			error = props.getProperty(this.error);

		addMark(TemplateParams.ERROR, error);

		addMark(TemplateParams.UNITS_LABEL, props.getProperty(TemplateComponent.UNITS));

		HashMap<String, Object> mapUnits = new HashMap<>();
		String units = "";
		int countUnits = 0;

		for (int i = 0; i < this.units.size(); i++) {
			if (this.units.get(i).isEnable()) {
				String logoUrl = this.units.get(i).getLogoUrl();

				if (logoUrl == null || logoUrl.isEmpty())
					logoUrl = baseUrl + this.configuration.getDefaultSpaceLogoPath();

				if (!hasPermission(this.units.get(i)))
					continue;

				mapUnits.put(TemplateParams.UNIT_LABEL, this.units.get(i).getLabel());
				mapUnits.put(TemplateParams.UNIT_LOGO_URL, logoUrl);

				try {
					mapUnits.put(TemplateParams.UNIT_URL, this.units.get(i).getUri().toURL().toString());
				} catch (MalformedURLException e) {
					this.logger.error("malformed unit URI", e);
				}

				units += block(TemplateParams.BLOCK_UNIT, mapUnits);
				countUnits++;
			}
		}

		if (countUnits > 0) {
			mapUnits.clear();
			mapUnits.put(TemplateParams.UNITS, units);
			addMark(TemplateParams.UNITS, block(TemplateParams.BLOCK_UNITS, mapUnits));
		} else {
			mapUnits.clear();
			mapUnits.put(TemplateParams.NO_SERVICE_ENABLE, props.getProperty(TemplateComponent.NO_SERVICE_ENABLE));
			addMark(TemplateParams.UNITS, block(TemplateParams.BLOCK_UNITS_EMPTY, mapUnits));
		}

		this.addLanguagesMark(lang);
	}

	private boolean hasPermission(BusinessUnit businessUnit) {
		String url = configuration.getSpaceBackserviceServletPath(businessUnit.getUri());
		String certFilename = configuration.getCertificatePath();
		String certPassword = configuration.getCertificatePassword();

		try {
			BackserviceApi api = new BackserviceApiImpl(url, certFilename, certPassword);
			String user = this.getParameterAsString(Parameter.USER);
			return api.hasPermissions(user);
		} catch (Throwable e) {
			if (logger != null) logger.debug("board render, user '"+Parameter.USER+"' not has permission to access to bussiness unit '"+ businessUnit.getId() +"', certificate filename '"+certFilename+"'. Message: "+ e.getMessage(), e);
			return false;
		}
	}

}
