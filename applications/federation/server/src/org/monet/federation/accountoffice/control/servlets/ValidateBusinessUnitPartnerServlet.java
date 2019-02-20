package org.monet.federation.accountoffice.control.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang.StringEscapeUtils;
import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.model.BusinessUnit;
import org.monet.federation.accountoffice.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class ValidateBusinessUnitPartnerServlet extends FederationServlet {
  private static final long serialVersionUID = 7546342624598034363L;
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
      String businessUnitId = StringEscapeUtils.escapeHtml(request.getParameter("business_unit"));
      String validationCode = StringEscapeUtils.escapeHtml(request.getParameter("validation_code"));
      Boolean exists = null;
      
      if (lang == null)
        lang = "en";

      BusinessUnit businessUnit = this.dataRepository.loadBusinessUnitById(businessUnitId);
      
      if (validationCode != null) {
        exists = this.dataRepository.existsBusinessUnitPartnerRequest(businessUnitId, validationCode);
        if (exists) {
          businessUnit.setEnable(true);
          this.dataRepository.saveBusinessUnit(businessUnit);
          this.dataRepository.removeBusinessUnitPartnerRequest(businessUnitId);
        }
      }

      this.templateComponent.createValidateBusinessUnitPartnerTemplate(response, businessUnit, Utils.getBaseUrl(request), exists, lang);
      
    } catch (Exception e) {
      this.logger.error(e.getMessage(), e);
    }
  }

}
