package org.monet.federation.accountoffice.control.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang.StringEscapeUtils;
import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.model.Federation;
import org.monet.federation.accountoffice.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class ValidateFederationTrustServlet extends FederationServlet {
  private static final long serialVersionUID = -7241823546597628405L;
  private DataRepository dataRepository;  

  @Inject
  public void injectDataRepository(DataRepository dataRepository) {
    this.dataRepository = dataRepository;
  }
  
  @Override
  protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      if (!this.isFederationRunning(response))
        return;
      
      this.initialize(response);
      
      String lang = (String) request.getSession().getAttribute("lang");
      String federationId = StringEscapeUtils.escapeHtml(request.getParameter("federation"));
      String validationCode = StringEscapeUtils.escapeHtml(request.getParameter("validation_code"));
      Boolean exists = null;
      
      if (lang == null)
        lang = "en";

      Federation federation = this.dataRepository.loadFederationById(federationId);
      
      if (validationCode != null) {
        exists = this.dataRepository.existsFederationTrustRequest(federationId, validationCode);
        if (exists) {
          federation.setTrusted(true);
          this.dataRepository.saveFederation(federation);
          this.dataRepository.removeFederationTrustRequest(federationId);
        }
      }
      
      this.templateComponent.createValidateFederationTrustTemplate(response, federation, Utils.getBaseUrl(request), exists, lang);

    } catch (Exception e) {
      this.logger.error(e.getMessage(), e);
    }
  }

}
