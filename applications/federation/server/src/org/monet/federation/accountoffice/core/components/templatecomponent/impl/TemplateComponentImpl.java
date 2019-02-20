package org.monet.federation.accountoffice.core.components.templatecomponent.impl;

import com.google.inject.Inject;
import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateComponent;
import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateFactory;
import org.monet.federation.accountoffice.core.components.templatecomponent.impl.renders.*;
import org.monet.federation.accountoffice.core.components.unitcomponent.BusinessUnitComponent;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.BusinessUnit;
import org.monet.federation.accountoffice.core.model.Federation;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class TemplateComponentImpl implements TemplateComponent {
    private Logger logger;
    private BusinessUnitComponent businessUnitComponent;
    private TemplateFactory templateFactory;
    private DataRepository dataRepository;
    private Configuration configuration;

    @Inject
    public void injectLogger(Logger logger) {
        this.logger = logger;
    }

    @Inject
    public void injectUnitComponent(BusinessUnitComponent businessUnitComponent) {
        this.businessUnitComponent = businessUnitComponent;
    }

    @Inject
    public void injectTemplateFactory(TemplateFactory templateFactory) {
        this.templateFactory = templateFactory;
    }

    @Inject
    public void injectDataRepository(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Inject
    public void injectConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void createTemplate(HttpServletResponse response, String path, boolean showLoginError, String redirectURL, String lang, String page, String urlrequest) {
    }

    @Override
    public void createLoginTemplate(HttpServletResponse response, boolean showLoginError, String requestToken, String lang, String baseUrl, int retries) {

        try {
            LoginRender render = (LoginRender) this.templateFactory.getRender(TemplateFactory.Renders.LOGIN);
            render.init(lang);
            render.setParameter(FederationRender.Parameter.REQUEST_TOKEN, requestToken);
            render.setParameter(FederationRender.Parameter.BASE_URL, baseUrl);
            render.setParameter(FederationRender.Parameter.SHOW_ERROR, showLoginError);
            render.setParameter(FederationRender.Parameter.SHOW_CAPTCHA, retries > 3);
            render.setParameter(FederationRender.Parameter.RETRIES, retries);

            String output = render.getOutput();
            PrintWriter out;
            out = response.getWriter();
            out.println(output);
            out.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void createRegisterTemplate(HttpServletResponse response, String path, boolean showRegisterError, String requestToken, String lang, String baseUrl, String additionalMessage) {
        try {
            RegisterRender render = (RegisterRender) this.templateFactory.getRender(TemplateFactory.Renders.REGISTER);
            render.init(lang);
            render.setParameter(FederationRender.Parameter.REQUEST_TOKEN, requestToken);
            render.setParameter(FederationRender.Parameter.BASE_URL, baseUrl);
            render.setParameter(FederationRender.Parameter.ADDITIONAL_MESSAGE, additionalMessage);
            render.setParameter(FederationRender.Parameter.SHOW_ERROR, showRegisterError);

            String output = render.getOutput();
            PrintWriter out;
            out = response.getWriter();
            out.println(output);
            out.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void createLoggedTemplate(HttpServletResponse response, String user, String error, String lang, String baseUrl) {
        try {
            BoardRender render = (BoardRender) this.templateFactory.getRender(TemplateFactory.Renders.BOARD);
            render.init(lang);
            render.setParameter(FederationRender.Parameter.USER, user);
            render.setParameter(FederationRender.Parameter.REQUEST_TOKEN, null);
            render.setParameter(FederationRender.Parameter.BASE_URL, baseUrl);

            List<BusinessUnit> unitList = new ArrayList<BusinessUnit>();
            for (BusinessUnit dataUnit : this.dataRepository.loadMemberBusinessUnits().getAll()) {
                BusinessUnit unit = businessUnitComponent.getBusinessUnit(dataUnit.getName());
                if (unit != null && unit.isEnable() && unit.isVisible()) unitList.add(unit);
            }
            render.addMoreParams(unitList, error);

            String output = render.getOutput();
            PrintWriter out;
            out = response.getWriter();
            out.println(output);
            out.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void createResetPasswordTemplate(HttpServletResponse response, String path, boolean showError, String token, String lang, String baseUrl, String additionalMessage) {
        try {
            ResetPasswordRender render = (ResetPasswordRender) this.templateFactory.getRender(TemplateFactory.Renders.RESET_PASSWORD);
            render.init(lang);
            render.setParameter(FederationRender.Parameter.REQUEST_TOKEN, token);
            render.setParameter(FederationRender.Parameter.BASE_URL, baseUrl);
            render.setParameter(FederationRender.Parameter.ADDITIONAL_MESSAGE, additionalMessage);
            render.setParameter(FederationRender.Parameter.SHOW_ERROR, showError);

            String output = render.getOutput();
            PrintWriter out;
            out = response.getWriter();
            out.println(output);
            out.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void createChangePasswordTemplate(HttpServletResponse response, String path, boolean showError, String token, String lang, String baseUrl, String additionalMessage) {
        try {
            ChangePasswordRender render = (ChangePasswordRender) this.templateFactory.getRender(TemplateFactory.Renders.CHANGE_PASSWORD);
            render.init(lang);
            render.setParameter(FederationRender.Parameter.REQUEST_TOKEN, token);
            render.setParameter(FederationRender.Parameter.BASE_URL, baseUrl);
            render.setParameter(FederationRender.Parameter.ADDITIONAL_MESSAGE, additionalMessage);
            render.setParameter(FederationRender.Parameter.SHOW_ERROR, showError);

            String output = render.getOutput();
            PrintWriter out;
            out = response.getWriter();
            out.println(output);
            out.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void createValidateFederationTrustTemplate(HttpServletResponse response, Federation federation, String baseUrl, Boolean result, String lang) {
        try {
            ValidateFederationTrustRender render = (ValidateFederationTrustRender) this.templateFactory.getRender(TemplateFactory.Renders.VALIDATE_FEDERATION_TRUST);
            render.setParameter(FederationRender.Parameter.FEDERATION, federation);
            render.setParameter(FederationRender.Parameter.BASE_URL, baseUrl);
            render.setParameter(FederationRender.Parameter.RESULT, result);
            render.init(lang);

            String output = render.getOutput();
            PrintWriter out;
            out = response.getWriter();
            out.println(output);
            out.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void createValidateBusinessUnitPartnerTemplate(HttpServletResponse response, BusinessUnit businessUnit, String baseUrl, Boolean result, String lang) {
        try {
            ValidateBusinessUnitPartnerRender render = (ValidateBusinessUnitPartnerRender) this.templateFactory.getRender(TemplateFactory.Renders.VALIDATE_BUSINESS_UNIT_PARTNER);
            render.setParameter(FederationRender.Parameter.BUSINESS_UNIT, businessUnit);
            render.setParameter(FederationRender.Parameter.BASE_URL, baseUrl);
            render.setParameter(FederationRender.Parameter.RESULT, result);
            render.init(lang);

            String output = render.getOutput();
            PrintWriter out;
            out = response.getWriter();
            out.print(output);
            out.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
