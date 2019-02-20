package org.monet.federation.accountoffice.core.components.templatecomponent.impl.renders;

import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateComponent;
import org.monet.federation.accountoffice.core.components.templatecomponent.impl.FederationRender;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.logger.Logger;

import java.util.HashMap;
import java.util.Properties;

public class ChangePasswordRender extends FederationRender {

	public ChangePasswordRender(Logger logger, Configuration configuration, String template) {
		super(logger, configuration, template);
	}

	@Override
	public void init(String lang) {
		this.lang = lang;
	}

	@Override
	protected void init() {
		Properties props = this.configuration.getLanguage(lang);
		boolean showError = (Boolean) this.getParameter(Parameter.SHOW_ERROR);
		String requestToken = this.getParameterAsString(Parameter.REQUEST_TOKEN);
		String baseUrl = this.getParameterAsString(Parameter.BASE_URL);
		String additionalMessage = (String)this.getParameter(Parameter.ADDITIONAL_MESSAGE);

		loadCanvas(TemplateParams.CANVAS_CHANGE_PASSWORD);

		addMark(TemplateParams.ORGANITATION, props.getProperty(TemplateComponent.ORGANIZATION));
		addMark(TemplateParams.ABOUT, props.getProperty(TemplateComponent.ABOUT));
		addMark(TemplateParams.COPYRIGHT, this.getCopyright(props));
		addMark(TemplateParams.PRIVACY, props.getProperty(TemplateComponent.PRIVACY));
		addMark(TemplateParams.CANCEL, props.getProperty(TemplateComponent.CANCEL));
		addMark(TemplateParams.RESET_PASSWORD, props.getProperty(TemplateComponent.RESET_PASSWORD));
		addMark(TemplateParams.CLOSE, props.getProperty(TemplateComponent.CLOSE));
		addMark(TemplateParams.CHANGE_PASSWORD_TITLE, props.getProperty(TemplateComponent.CHANGE_PASSWORD_TITLE));
		addMark(TemplateParams.CHANGE_PASSWORD_SEND, props.getProperty(TemplateComponent.CHANGE_PASSWORD_SEND));
		addMark(TemplateParams.POWERED_BY, props.getProperty(TemplateComponent.POWERED_BY));
		addMark(TemplateParams.OLD_PASSWORD, props.getProperty(TemplateComponent.OLD_PASSWORD));
		addMark(TemplateParams.NEW_PASSWORD, props.getProperty(TemplateComponent.NEW_PASSWORD));
		addMark(TemplateParams.USER, props.getProperty(TemplateComponent.USER));

		addMark(TemplateParams.TOKEN, requestToken);
		addMark(TemplateParams.LOGO, baseUrl + "/accounts/authorization/resources/?id=logo");
		addMark(TemplateParams.URL_FEDERATION, baseUrl);            //baseUrl
		addMark(TemplateParams.ACTION, baseUrl + "/accounts/authorization/?action=changepassword");
		addMark(TemplateParams.CHANGE_PASSWORD_RESULT_ACTIONS, "");
		addMark(TemplateParams.HOME, props.getProperty(TemplateComponent.HOME));
		addMark(TemplateParams.ACTION_HOME, baseUrl + "/accounts/authorization/home");

		if (showError) {
			boolean error;
			HashMap<String, Object> map = new HashMap<String, Object>();

			if (additionalMessage != null) {
				if (additionalMessage.equals("errorUnknow"))
					map.put("result", props.getProperty(TemplateComponent.ERROR));
				else {
					map.put("result", props.getProperty(TemplateComponent.SUCCESSFULLY_CHANGE_PASSWORD));
					map.put(TemplateParams.HOME, props.getProperty(TemplateComponent.HOME));
					map.put(TemplateParams.ACTION_HOME, baseUrl + "/accounts/authorization/home");
					addMark(TemplateParams.CHANGE_PASSWORD_RESULT_ACTIONS, block(TemplateParams.BLOCK_CHANGE_PASSWORD_RESULT_SUCCESS_ACTIONS, map));
				}
				error = false;
			} else {
				map.put("result", props.getProperty(TemplateComponent.ERROR_CHANGE_PASSWORD));
				error = true;
			}

			if (error)
				addMark(TemplateParams.CHANGE_PASSWORD_RESULT, block(TemplateParams.BLOCK_CHANGE_PASSWORD_RESULT_FAILURE, map));
			else
				addMark(TemplateParams.CHANGE_PASSWORD_RESULT, block(TemplateParams.BLOCK_CHANGE_PASSWORD_RESULT_SUCCESS, map));
		} else
			addMark(TemplateParams.CHANGE_PASSWORD_RESULT, "");

		this.addLanguagesMark(lang);
	}
}
