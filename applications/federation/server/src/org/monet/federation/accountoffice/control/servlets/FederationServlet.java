package org.monet.federation.accountoffice.control.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.monet.federation.accountoffice.control.servlets.actions.ActionFactory;
import org.monet.federation.accountoffice.core.components.accountcomponent.SessionComponent;
import org.monet.federation.accountoffice.core.components.certificatecomponent.CertificateComponent;
import org.monet.federation.accountoffice.core.components.requesttokencomponent.RequestTokenComponent;
import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateComponent;
import org.monet.federation.accountoffice.core.components.unitcomponent.BusinessUnitComponent;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.layers.account.AccountLayer;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.setupservice.core.model.FederationStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public abstract class FederationServlet extends HttpServlet {
    private static final long serialVersionUID = 3534663941910208314L;

    protected FederationStatus status;
    protected Logger logger;
    protected Configuration configuration;
    protected RequestTokenComponent requestTokenComponent;
    protected TemplateComponent templateComponent;
    protected ActionFactory actionFactory;
    protected SessionComponent sessionComponent;
    protected BusinessUnitComponent businessUnitComponent;
    protected AccountLayer accountLayer;
    protected CertificateComponent certificateComponent;

    @Inject
    public void injectLogger(Logger logger) {
        this.logger = logger;
    }

    @Inject
    public void injectConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Inject
    public void injectRequestToken(RequestTokenComponent requestToken) {
        this.requestTokenComponent = requestToken;
    }

    @Inject
    public void injectTemplateComponent(TemplateComponent templateComp) {
        this.templateComponent = templateComp;
    }

    @Inject
    public void injectActionFactory(ActionFactory actinFactory) {
        this.actionFactory = actinFactory;
    }

    @Inject
    public void injectAccountComponent(SessionComponent accComp) {
        this.sessionComponent = accComp;
    }

    @Inject
    public void injectAccountLayer(AccountLayer accountLayer) {
        this.accountLayer = accountLayer;
    }

    @Inject
    public void injectFederationStatus(FederationStatus status) {
        this.status = status;
    }

    @Inject
    public void injectUnitComponent(BusinessUnitComponent businessUnitComponent) {
        this.businessUnitComponent = businessUnitComponent;
    }

    @Inject
    public void injectVerificationComponent(CertificateComponent verificationComponent) {
        this.certificateComponent = verificationComponent;
    }

    protected void initialize(HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
    }

    protected boolean isFederationRunning(HttpServletResponse response) throws IOException {
        if (status.isRunning())
            return true;
        response.getOutputStream().write("Federation is stop".getBytes());
        return false;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("doPost(%s, %s)", req, resp);
        doProcess(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("doGet(%s, %s)", req, resp);
        doProcess(req, resp);
    }

    protected abstract void doProcess(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
