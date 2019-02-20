package org.monet.federation.accountoffice.control.servlets;

import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.apache.commons.lang.StringEscapeUtils;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.guice.InjectorFactory;
import org.monet.federation.accountoffice.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Singleton
public class HomeServlet extends FederationServlet {
    private static final long serialVersionUID = 2659656339455303815L;

    @Override
    protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!this.isFederationRunning(response))
            return;

        this.initialize(response);

        String requestURL;
        HttpSession session = request.getSession();
        String user = (String) session.getAttribute("user");
        String lang = (String) session.getAttribute("lang");

        Injector injector = InjectorFactory.get();
        Configuration configuration = injector.getInstance(Configuration.class);

        String baseUrl = Utils.getBaseUrl(request);

        if (user == null) {
            String params = "";
            if ((request.getQueryString() != null) && (! request.getQueryString().equals(""))) params = "?" + request.getQueryString();

            Utils.sendRedirect(response, baseUrl + "/accounts/authorization/" + params);
            return;
        } else {
            String action = StringEscapeUtils.escapeHtml(request.getParameter("action"));

            if ((action != null) && (action.equals("changelanguage"))) {
                this.actionFactory.getAction(action).execute(request, response, this.getServletContext().getRealPath(""));
                return;
            }
        }

        this.templateComponent.createLoggedTemplate(response, user, null, lang, baseUrl);
    }

}
